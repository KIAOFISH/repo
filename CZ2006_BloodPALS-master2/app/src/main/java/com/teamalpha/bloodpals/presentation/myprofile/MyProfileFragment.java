package com.teamalpha.bloodpals.presentation.myprofile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MyProfileFragment extends DaggerFragment {

    private static final String TAG = "MyProfileFragment";

    private NavController navController;

    private MyProfileViewModel viewModel;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView displayName, displayEmail, displayDOB, displayBloodType, displayPoints;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                navController.navigate(R.id.action_nav_my_profile_to_nav_edit_my_profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: MyProfileFragment was created...");

        displayName = view.findViewById(R.id.display_name);
        displayEmail = view.findViewById(R.id.display_email);
        displayDOB = view.findViewById(R.id.display_dob);
        displayBloodType = view.findViewById(R.id.display_blood_type);
        displayPoints = view.findViewById(R.id.display_points);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        view.findViewById(R.id.button_view_my_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View My QR Code button clicked.");
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_my_profile_to_nav_my_qr_code);
            }
        });

        viewModel = ViewModelProviders.of(this, providerFactory).get(MyProfileViewModel.class);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        subscribeToastMessage();

        subscribeIsInProgress();

        subscribeUser();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadUserData();
            }
        });

        viewModel.loadUserData();
        swipeRefreshLayout.setRefreshing(true);
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

    private void subscribeUser() {
        viewModel.observeUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null) {
                    displayName.setText(user.getName());
                    displayEmail.setText(user.getEmail());

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                    dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    if(user.getDateOfBirth() != null) displayDOB.setText(dateFormatter.format(user.getDateOfBirth()));
                    else displayDOB.setText("(not set)");

                    if(user.getBloodType() != null) displayBloodType.setText(user.getBloodType());
                    else displayBloodType.setText("(not set)");

                    displayPoints.setText(String.valueOf(user.getPoints()));

                    if(swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
                else {
                    navController.popBackStack();
                }
            }
        });
    }

}
