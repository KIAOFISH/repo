package com.teamalpha.bloodpals.presentation.userprofile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import javax.inject.Inject;

public class ScanQRCodeViewModel extends ViewModel {

    private static final String TAG = "ScanQRCodeViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();

    private SessionManager sessionManager;

    @Inject
    public ScanQRCodeViewModel(SessionManager sessionManager) {
        this.sessionManager = sessionManager;

        Log.d(TAG, "ScanQRCodeViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public boolean validateScanResult(String result, int currentDestinationId) {
        if(result.trim().toLowerCase().equals(sessionManager.getUser().getValue().data.getId().toLowerCase())) {
            if(currentDestinationId == R.id.nav_give_user_points)
                toastMessageLiveData.setValue(new TextStringResource("You cannot give points to yourself!"));
            else
                toastMessageLiveData.setValue(new TextStringResource("You cannot deduct points from yourself!"));
            return false;
        }
        return true;
    }

}
