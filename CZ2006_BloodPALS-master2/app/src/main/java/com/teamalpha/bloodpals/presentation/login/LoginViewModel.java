package com.teamalpha.bloodpals.presentation.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.auth.AuthManager;
import com.teamalpha.bloodpals.logic.auth.AuthUser;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";

    private AuthManager authManager;

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthManager authManager) {
        this.authManager = authManager;

        Log.d(TAG, "LoginViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public void login(String email, String password) {
        Log.d(TAG, "login: auth started");
        isInProgressLiveData.setValue(true);
        authManager.login(email, password, new OnCompleteListener<AuthUser>() {
            @Override
            public void onComplete(@NonNull Task<AuthUser> task) {
                Log.d(TAG, "login: auth completed");
                if(task.isSuccessful()) {
                    Log.d(TAG, "login: auth success");
                }
                else {
                    Log.e(TAG, "login: auth ERROR - "+task.getException());
                    isInProgressLiveData.setValue(false);
                    toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_login_fail));
                }
            }
        });
    }
}
