package com.teamalpha.bloodpals.logic.bloodbanks;

import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApiResponse;
import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApiService;

import javax.inject.Inject;

import retrofit2.Call;

public class BloodBanksManager {

    private static final String TAG = "BloodBanksManager";

    private BloodBanksApiService bloodBanksApiService;

    @Inject
    public BloodBanksManager(BloodBanksApiService bloodBanksApiService) {
        this.bloodBanksApiService = bloodBanksApiService;
    }

    public Call<BloodBanksApiResponse> getInformationOnBloodBanks() {
        return bloodBanksApiService.getInformationOnBloodBanks();
    }

}
