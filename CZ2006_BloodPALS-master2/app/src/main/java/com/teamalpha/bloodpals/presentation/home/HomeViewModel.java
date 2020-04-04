package com.teamalpha.bloodpals.presentation.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    private MutableLiveData<String> mText;

    @Inject
    public HomeViewModel() {
        Log.d(TAG, "HomeViewModel: viewmodel is ready...");
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}