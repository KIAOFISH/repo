package com.teamalpha.bloodpals.presentation.register;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.logic.auth.AuthManager;
import com.teamalpha.bloodpals.logic.auth.AuthUser;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import java.util.ArrayList;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {

    private static final String TAG = "RegisterViewModel";

    private AuthManager authManager;
    private UserManager userManager;

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();

    @Inject
    public RegisterViewModel(AuthManager authManager, UserManager userManager) {
        this.authManager = authManager;
        this.userManager = userManager;

        Log.d(TAG, "RegisterViewModel: view model is ready...");
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<Boolean> observeIsInProgress() {
        return isInProgressLiveData;
    }

    public void register(final String name, final String email, final String password) {
        Log.d(TAG, "register: auth started");
        isInProgressLiveData.setValue(true);
        authManager.unSubscribe(); // unsubscribe to auth state changed listener
        authManager.register(email, password, new OnCompleteListener<AuthUser>() {
            @Override
            public void onComplete(@NonNull Task<AuthUser> task) {
                Log.d(TAG, "register: auth completed");
                if(task.isSuccessful()) {
                    Log.d(TAG, "register: auth success");
                    createUser(task.getResult(), name);
                }
                else {
                    Log.e(TAG, "register: auth ERROR - "+task.getException());
                    isInProgressLiveData.setValue(false);
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                    authManager.subscribe(); // resubscribe to auth state changed listener
                }
            }
        });
    }

    private void createUser(AuthUser authUser, String name) {
        Log.d(TAG, "register: creating user id "+authUser.getId());
        User newUser = new User(
                authUser.getId(),
                authUser.getEmail(),
                name,
                null,
                null,
                0,
                new ArrayList<String>()
        );
        userManager.setUser(authUser.getId(), newUser).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "register: creating user success");
                    //isInProgressLiveData.setValue(false);
                    toastMessageLiveData.setValue(new TextStringResource("Register success."));
                }
                else {
                    Log.e(TAG, "register: creating user ERROR - "+task.getException());
                    isInProgressLiveData.setValue(false);
                    toastMessageLiveData.setValue(new TextStringResource(task.getException().getMessage()));
                }
                authManager.subscribe(); // resubscribe to auth state changed listener
            }
        });
    }
}
