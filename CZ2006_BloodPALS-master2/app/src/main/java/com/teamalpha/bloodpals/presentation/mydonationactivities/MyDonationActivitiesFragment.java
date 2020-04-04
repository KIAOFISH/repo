package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.DonationActivity;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.VerticalSpacingItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MyDonationActivitiesFragment extends DaggerFragment implements MyDonationActivitiesRecyclerAdapter.OnClickListener {

    private static final String TAG = "MyDonationActivitiesFragment";

    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView displayNextEarliestDonationDate;

    private NavController navController;

    private MyDonationActivitiesViewModel viewModel;

    @Inject
    MyDonationActivitiesRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_my_donation_activities, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_donation_activities, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) searchItem.collapseActionView();
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.setQuery("", true);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_new:
                navController.navigate(R.id.action_nav_my_donation_activities_to_nav_create_donation_activity);
                return true;
            case R.id.action_sort_date_time_ascending:
                adapter.sortByDateTimeAscending();
                return true;
            case R.id.action_sort_date_time_descending:
                adapter.sortByDateTimeDescending();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: MyDonationActivitiesFragment was created...");

        recyclerView = view.findViewById(R.id.donation_activity_list_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        displayNextEarliestDonationDate = view.findViewById(R.id.display_next_earliest_donation_date);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MyDonationActivitiesViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        initRecyclerView();

        subscribeToastMessage();

        subscribeIsInProgress();

        subscribeUserDonationActivities();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadUserDonationActivities();
            }
        });

        viewModel.loadUserDonationActivities();
        swipeRefreshLayout.setRefreshing(true);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void subscribeToastMessage() {
        viewModel.observeToastMessage().observe(getViewLifecycleOwner(), new Observer<StringResource>() {
            @Override
            public void onChanged(StringResource stringResource) {
                if(stringResource != null)
                    Toast.makeText(getActivity().getApplicationContext(), stringResource.format(getActivity().getApplicationContext()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribeIsInProgress() {
        viewModel.observeIsInProgress().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean && !swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(true);
                if(!aBoolean && swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void subscribeUserDonationActivities(){
        viewModel.observeUserDonationActivities().observe(getViewLifecycleOwner(), new Observer<List<DonationActivity>>() {
            @Override
            public void onChanged(List<DonationActivity> donationActivities) {
                Log.d(TAG, "onChanged: donation activities list changed.");

                adapter.setDonationActivityList(donationActivities);

                updateNextEarliestDonationDate(donationActivities);

                if(swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateNextEarliestDonationDate(List<DonationActivity> donationActivities) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        if(donationActivities.size() > 0) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.setTime(donationActivities.get(0).getActivityDateTime());
            calendar.add(Calendar.MONTH, 3);
            displayNextEarliestDonationDate.setText(dateFormatter.format(calendar.getTime()));
        }
        else {
            Date todayDate = new Date();
            displayNextEarliestDonationDate.setText(dateFormatter.format(todayDate));
        }
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: position "+position);
        DonationActivity donationActivity = adapter.getDonationActivity(position);
        Log.d(TAG, "onItemClick: donation activity id = "+donationActivity.getId());
        Bundle bundle = new Bundle();
        bundle.putString("donationActivityId", donationActivity.getId());
        navController.navigate(R.id.action_nav_my_donation_activities_to_nav_edit_donation_activity, bundle);
    }
}
