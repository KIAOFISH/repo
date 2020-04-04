package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.user.DonationActivity;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import java.util.List;

import javax.inject.Inject;

public class MyDonationActivitiesViewModel extends ViewModel {

    private static final String TAG = "MyDonationActivitiesViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DonationActivity>> userDonationActivities = new MutableLiveData<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public MyDonationActivitiesViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "MyDonationActivitiesViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public LiveData<List<DonationActivity>> observeUserDonationActivities() {
        return userDonationActivities;
    }

    public void loadUserDonationActivities() {
        Log.d(TAG, "loadUserDonationActivities: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;

        userManager.getUserDonationActivities(user.getId()).addOnCompleteListener(new OnCompleteListener<List<DonationActivity>>() {
            @Override
            public void onComplete(@NonNull Task<List<DonationActivity>> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "loadUserDonationActivities: success");
                    userDonationActivities.setValue(task.getResult());
                }
                else {
                    Log.e(TAG, "loadUserDonationActivities: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });
    }

}
