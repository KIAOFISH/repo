package com.teamalpha.bloodpals.presentation.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.presentation.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private HomeViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: HomeFragment was created...");

        view.findViewById(R.id.button_blood_donation_process).setOnClickListener(this);
        view.findViewById(R.id.button_blood_facts_and_figures).setOnClickListener(this);
        view.findViewById(R.id.button_how_to_maintain_healthy_iron_levels).setOnClickListener(this);
        view.findViewById(R.id.button_blood_transfusion).setOnClickListener(this);
        view.findViewById(R.id.button_type_of_blood_donation).setOnClickListener(this);

        viewModel = ViewModelProviders.of(this, providerFactory).get(HomeViewModel.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_blood_donation_process:
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsa.gov.sg/blood-donation/donation-process"));
                startActivity(browserIntent1);
                break;
            case R.id.button_blood_facts_and_figures:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsa.gov.sg/blood-donation/blood-facts-and-figures"));
                startActivity(browserIntent2);
                break;
            case R.id.button_how_to_maintain_healthy_iron_levels:
                Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsa.gov.sg/blood-donation/maintain-healthy-iron-levels"));
                startActivity(browserIntent3);
                break;
            case R.id.button_blood_transfusion:
                Intent browserIntent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsa.gov.sg/blood-donation/blood-transfusion"));
                startActivity(browserIntent4);
                break;
            case R.id.button_type_of_blood_donation:
                Intent browserIntent5 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hsa.gov.sg/blood-donation/types-of-blood-donations"));
                startActivity(browserIntent5);
                break;
        }
    }
}
