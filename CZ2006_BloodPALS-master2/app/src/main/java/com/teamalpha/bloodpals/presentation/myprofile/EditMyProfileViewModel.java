package com.teamalpha.bloodpals.presentation.myprofile;

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
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import java.util.Date;

import javax.inject.Inject;

public class EditMyProfileViewModel extends ViewModel {

    private static final String TAG = "EditMyProfileViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> isUpdatedLiveData = new SingleLiveEvent<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public EditMyProfileViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "EditMyProfileViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public LiveData<User> observeUser() {
        return userLiveData;
    }

    public LiveData<Boolean> observeIsUpdated() {
        return isUpdatedLiveData;
    }

    public void loadUserData() {
        Log.w(TAG, "loadUserData: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;

        try {
            userManager.getUserById(user.getId()).addOnCompleteListener(new OnCompleteListener<User>() {
                @Override
                public void onComplete(@NonNull Task<User> task) {
                    if(task.isSuccessful()) {
                        User user = task.getResult();
                        if(user == null) {
                            Log.w(TAG, "loadUserData: ERROR - No user found");
                            toastMessageLiveData.setValue(new TextStringResource("Invalid scan code!"));
                        }
                        else {
                            Log.w(TAG, "loadUserData: success");
                            sessionManager.update(user);
                        }
                        userLiveData.setValue(user);
                    }
                    else {
                        Log.e(TAG, "loadUserData: ERROR - "+task.getException().getMessage());
                        toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_user_exception));
                        userLiveData.setValue(null);
                    }
                    isInProgressLiveData.setValue(false);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadUserData: ERROR - "+e.getMessage());
            toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_user_exception));
            userLiveData.setValue(null);
            isInProgressLiveData.setValue(false);
        }
    }

    public void updateUserData(String name, @Nullable Date dob, @Nullable String bloodType) {
        Log.d(TAG, "updateUserData: started");
        isInProgressLiveData.setValue(true);

        User user = sessionManager.getUser().getValue().data;
        User newUser = new User(user.getId(), user.getEmail(), name, dob, bloodType, user.getPoints(), user.getRoles());

        userManager.updateUserProfile(user.getId(), newUser).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()) {
                    if(task.getResult()) {
                        isUpdatedLiveData.setValue(true);
                        toastMessageLiveData.setValue(new TextStringResource("Profile successfully updated."));
                    }
                    else {
                        Log.e(TAG, "updateUserData: ERROR - Updating of profile failed!");
                        toastMessageLiveData.setValue(new TextStringResource("Updating of profile failed! Please try again later."));
                    }
                }
                else {
                    Log.e(TAG, "updateUserData: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });
    }

}
