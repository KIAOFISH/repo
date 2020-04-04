package com.teamalpha.bloodpals.presentation.sample;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.data.bloodbanks.Record;
import com.teamalpha.bloodpals.data.firebase.RewardDoc;
import com.teamalpha.bloodpals.logic.reward.Reward;
import com.teamalpha.bloodpals.logic.reward.RewardManager;
import com.teamalpha.bloodpals.presentation.utils.VerticalSpacingItemDecoration;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import android.widget.Toast;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class RewardFragment extends DaggerFragment {

    private static final String TAG = "RewardFragment";

    private RewardViewModel viewModel;

    private RecyclerView recyclerView;

    @Inject
    RewardRecyclerAdapter adapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.listRecyclerView);

        viewModel = ViewModelProviders.of(this, providerFactory).get(RewardViewModel.class);


        initRecyclerView();
        subscribeToastMessage();
        subscribeObservers();
        viewModel.loadData();

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

    private void initRecyclerView(){
        adapter=new  RewardRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        VerticalSpacingItemDecoration itemDecoration = new VerticalSpacingItemDecoration(15);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

    }

    private void subscribeObservers(){
        viewModel.getRewardLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.getRewardLiveData().observe(getViewLifecycleOwner(), (Observer<? super List<Reward>>) new Observer<List<Reward>>() {


            @Override
            public void onChanged(List<Reward> rewardDoc) {
                Log.d(TAG, "onChanged: Rewardlist changed.");
                adapter.setRewardList(rewardDoc);

            }
        });
    }
}