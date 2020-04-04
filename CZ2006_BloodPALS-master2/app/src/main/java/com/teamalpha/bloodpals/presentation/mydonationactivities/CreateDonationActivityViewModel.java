package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.Date;

import javax.inject.Inject;

public class CreateDonationActivityViewModel extends ViewModel {

    private static final String TAG = "CreateDonationActivityViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> isCreatedLiveData = new SingleLiveEvent<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public CreateDonationActivityViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "CreateDonationActivityViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public LiveData<Boolean> observeIsCreated() {
        return isCreatedLiveData;
    }

    public void createDonationActivity(Date activityDateTime, String location, @Nullable float donatedAmount) {
        Log.d(TAG, "createDonationActivity: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;
        DonationActivity newDonationActivity = new DonationActivity(null, activityDateTime, location, donatedAmount);

        userManager.addDonationActivity(user.getId(), newDonationActivity).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()) {
                    if(task.getResult()) {
                        isCreatedLiveData.setValue(true);
                        toastMessageLiveData.setValue(new TextStringResource("Donation activity successfully created."));
                    }
                    else {
                        Log.e(TAG, "createDonationActivity: ERROR - Creation of donation activity failed!");
                        toastMessageLiveData.setValue(new TextStringResource("Creation of donation activity failed! Please try again later."));
                    }
                }
                else {
                    Log.e(TAG, "createDonationActivity: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });

    }

}
