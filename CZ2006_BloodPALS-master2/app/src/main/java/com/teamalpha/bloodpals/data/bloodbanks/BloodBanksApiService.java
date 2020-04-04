package com.teamalpha.bloodpals.data.bloodbanks;

import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApi;
import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApiResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;

public class BloodBanksApiService {

    private static final String TAG = "BloodBanksApiService";

    private static final String RESOURCE_ID = "c2f8f2c0-d7ad-4c9e-8d8b-250c342a1d6c";

    private Retrofit retrofit;

    private BloodBanksApi bloodBanksApi;

    @Inject
    public BloodBanksApiService(Retrofit retrofit) {
        this.retrofit = retrofit;
        bloodBanksApi = retrofit.create(BloodBanksApi.class);
    }

    public Call<BloodBanksApiResponse> getInformationOnBloodBanks() {
        return bloodBanksApi.getInformationOnBloodBanks(RESOURCE_ID, null, null);
    }

}
