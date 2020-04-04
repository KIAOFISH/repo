package com.teamalpha.bloodpals.presentation.myprofile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.session.SessionResource;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import javax.inject.Inject;

public class MyProfileViewModel extends ViewModel {

    private static final String TAG = "MyProfileViewModel";

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    private SessionManager sessionManager;
    private UserManager userManager;

    @Inject
    public MyProfileViewModel(SessionManager sessionManager, UserManager userManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        Log.d(TAG, "MyProfileViewModel: view model is ready...");
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

    public User getUser() {
        return sessionManager.getUser().getValue().data;
    }

}
