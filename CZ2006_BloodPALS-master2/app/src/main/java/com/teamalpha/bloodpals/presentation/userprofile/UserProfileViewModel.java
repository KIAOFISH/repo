package com.teamalpha.bloodpals.presentation.userprofile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.auth.AuthUser;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import javax.inject.Inject;

public class UserProfileViewModel extends ViewModel {

    private static final String TAG = "UserProfileViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public UserProfileViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "UserProfileViewModel: view model is ready...");
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

    public void loadUserData(String userId) {
        isInProgressLiveData.setValue(true);
        try {
            userManager.getUserById(userId).addOnCompleteListener(new OnCompleteListener<User>() {
                @Override
                public void onComplete(@NonNull Task<User> task) {
                    if(task.isSuccessful()) {
                        User user = task.getResult();
                        if(user == null) {
                            Log.w(TAG, "loadData: ERROR - No user found");
                            toastMessageLiveData.setValue(new TextStringResource("Invalid scan code!"));
                        }
                        userLiveData.setValue(user);
                    }
                    else {
                        Log.e(TAG, "loadData: ERROR - "+task.getException().getMessage());
                        toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_user_exception));
                        userLiveData.setValue(null);
                    }
                    isInProgressLiveData.setValue(false);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "loadData: ERROR - "+e.getMessage());
            toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_user_exception));
            userLiveData.setValue(null);
            isInProgressLiveData.setValue(false);
        }
    }

    public void addPoints(int pointsToAdd) {
        Log.d(TAG, "addPoints: started");
        isInProgressLiveData.setValue(true);
        User user = sessionManager.getUser().getValue().data;
        userManager.addUserPoints(
                userLiveData.getValue().getId(), pointsToAdd, user.getId(), user.getName(), user.getEmail()
        ).addOnCompleteListener(new OnCompleteListener<Long>() {
            @Override
            public void onComplete(@NonNull Task<Long> task) {
                if(task.isSuccessful()) {
                    User updateUser = userLiveData.getValue();
                    updateUser.setPoints(task.getResult());
                    userLiveData.setValue(updateUser);
                    Log.d(TAG, "addPoints: updated points = "+task.getResult());
                    toastMessageLiveData.setValue(new TextStringResource("Points successfully given to user."));
                }
                else {
                    Log.e(TAG, "addPoints: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });
    }

    public void deductPoints(int pointsToDeduct) {
        Log.d(TAG, "deductPoints: started");
        isInProgressLiveData.setValue(true);
        User user = sessionManager.getUser().getValue().data;
        userManager.deductUserPoints(
                userLiveData.getValue().getId(), pointsToDeduct, user.getId(), user.getName(), user.getEmail()
        ).addOnCompleteListener(new OnCompleteListener<Long>() {
            @Override
            public void onComplete(@NonNull Task<Long> task) {
                if(task.isSuccessful()) {
                    User updateUser = userLiveData.getValue();
                    updateUser.setPoints(task.getResult());
                    userLiveData.setValue(updateUser);
                    Log.d(TAG, "deductPoints: updated points = "+task.getResult());
                    toastMessageLiveData.setValue(new TextStringResource("Points successfully deduct from user."));
                }
                else {
                    Log.e(TAG, "deductPoints: ERROR - "+task.getException().getMessage());
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                isInProgressLiveData.setValue(false);
            }
        });
    }

}
