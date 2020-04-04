package com.teamalpha.bloodpals.presentation.bloodbanks;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.teamalpha.bloodpals.data.bloodbanks.Record;
import com.teamalpha.bloodpals.data.bloodbanks.BloodBanksApiResponse;
import com.teamalpha.bloodpals.logic.bloodbanks.BloodBank;
import com.teamalpha.bloodpals.logic.bloodbanks.BloodBankConverter;
import com.teamalpha.bloodpals.logic.bloodbanks.BloodBanksManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BloodBanksViewModel extends ViewModel {

    private static final String TAG = "BloodBanksViewModel";

    private BloodBanksManager bloodBanksManager;
    private List<Record> recordData = new ArrayList<>();

    private final MutableLiveData<List<BloodBank>> bloodBanksLiveData = new MutableLiveData<>();

    @Inject
    public BloodBanksViewModel(BloodBanksManager bloodBanksManager) {
        this.bloodBanksManager = bloodBanksManager;
        Log.d(TAG, "BloodBanksViewModel: view model is ready...");
    }

    public LiveData<List<BloodBank>> getBloodBankData() {
        return bloodBanksLiveData;
    }

    public void loadBloodBanks() {
        Call<BloodBanksApiResponse> call = bloodBanksManager.getInformationOnBloodBanks();
        call.enqueue(new Callback<BloodBanksApiResponse>() {

            @Override
            public void onResponse(Call<BloodBanksApiResponse> call, Response<BloodBanksApiResponse> response) {
                if (response.code() == 200 &&
                        response.body().isSuccess()) {
                    recordData = response.body().getResult().getRecords();
                    bloodBanksLiveData.setValue(BloodBankConverter.convertRecordToBloodBank(recordData));
                }
                else {
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<BloodBanksApiResponse> call, Throwable t) {
                Log.e(TAG, call.toString());
            }

        });
    }
}
