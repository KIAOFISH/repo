package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.user.DonationActivity;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import java.util.Date;

import javax.inject.Inject;

public class EditDonationActivityViewModel extends ViewModel {

    private static final String TAG = "EditDonationActivityViewModel";

    private String activityDonationId;

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<DonationActivity> donationActivityLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> isUpdatedLiveData = new SingleLiveEvent<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public EditDonationActivityViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "EditDonationActivityViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public LiveData<DonationActivity> observeDonationActivity() {
        return donationActivityLiveData;
    }

    public LiveData<Boolean> observeIsUpdated() {
        return isUpdatedLiveData;
    }

    public void loadDonationActivity(String activityDonationId) {
        Log.d(TAG, "loadDonationActivity: started");
        isInProgressLiveData.setValue(true);

        this.activityDonationId = activityDonationId;
        User user = sessionManager.getUser().getValue().data;

        try {
            userManager.getUserDonationActivity(user.getId(), activityDonationId).addOnCompleteListener(new OnCompleteListener<DonationActivity>() {
                @Override
                public void onComplete(@NonNull Task<DonationActivity> task) {
                    if(task.isSuccessful()) {
                        DonationActivity donationActivity = task.getResult();
                        if(donationActivity == null) {
                            Log.w(TAG, "loadDonationActivity: ERROR - No donation activity found");
                            toastMessageLiveData.setValue(new TextStringResource("Donation Activity not found! Please try again."));
                        }
                        donationActivityLiveData.setValue(donationActivity);
                    }
                    else {
                        Log.e(TAG, "loadDonationActivity: ERROR - "+task.getException().getMessage());
                        toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_donation_activity_exception));
                        donationActivityLiveData.setValue(null);
                    }
                    isInProgressLiveData.setValue(false);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadDonationActivity: ERROR - "+e.getMessage());
            toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_donation_activity_exception));
            donationActivityLiveData.setValue(null);
            isInProgressLiveData.setValue(false);
        }
    }

    public void updateDonationActivity(Date activityDateTime, String location, @Nullable float donatedAmount) {
        Log.d(TAG, "updateDonationActivity: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;
        DonationActivity updateDonationActivity = new DonationActivity(activityDonationId, activityDateTime, location, donatedAmount);

        userManager.updateDonationActivity(user.getId(), updateDonationActivity).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()) {
                    if(task.getResult()) {
                        isUpdatedLiveData.setValue(true);
                        toastMessageLiveData.setValue(new TextStringResource("Donation activity successfully updated."));
                    }
                    else {
                        Log.e(TAG, "updateDonationActivity: ERROR - Updating of donation activity failed!");
                        toastMessageLiveData.setValue(new TextStringResource("Updating of donation activity failed! Please try again later."));
                    }
                }
                else {
                    Log.e(TAG, "updateDonationActivity: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });

    }

    public void deleteDonationActivity() {
        Log.d(TAG, "deleteDonationActivity: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;

        userManager.deleteDonationActivity(user.getId(), activityDonationId).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()) {
                    if(task.getResult()) {
                        isUpdatedLiveData.setValue(true);
                        toastMessageLiveData.setValue(new TextStringResource("Donation activity successfully deleted."));
                    }
                    else {
                        Log.e(TAG, "deleteDonationActivity: ERROR - Deletion of donation activity failed!");
                        toastMessageLiveData.setValue(new TextStringResource("Deletion of donation activity failed! Please try again later."));
                    }
                }
                else {
                    Log.e(TAG, "deleteDonationActivity: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });
    }
}
