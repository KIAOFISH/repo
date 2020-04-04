package com.teamalpha.bloodpals.presentation;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.auth.AuthManager;
import com.teamalpha.bloodpals.logic.session.SessionResource;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.auth.AuthUser;
import com.teamalpha.bloodpals.logic.user.User;
import com.teamalpha.bloodpals.logic.user.UserManager;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;

import javax.inject.Inject;

public class MainViewModel extends ViewModel implements AuthManager.AuthStateListener {

    private static final String TAG = "AuthViewModel";

    private AuthManager authManager;
    private UserManager userManager;
    private SessionManager sessionManager;

    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private final MutableLiveData<SessionResource<User>> sessionUserLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(AuthManager authManager, UserManager userManager, SessionManager sessionManager) {
        this.authManager = authManager;
        this.userManager = userManager;
        this.sessionManager = sessionManager;
        this.authManager.setAuthStateListener(this);
    }

    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<SessionResource<User>> observeSessionUser(){
        return sessionManager.getUser();
    }

    public void subscribeAuthListener() {
        authManager.subscribe();
    }

    public void unSubscribeAuthListener() {
        authManager.unSubscribe();
    }

    public void authLogout() {
        authManager.signOut();
    }

    @Override
    public void onAuthStateChanged(boolean isAuthorized) {
        if(isAuthorized) {
            final AuthUser authUser = authManager.getUser();

            try {
                userManager.getUserById(authUser.getId()).addOnCompleteListener(new OnCompleteListener<User>() {
                    @Override
                    public void onComplete(@NonNull Task<User> task) {
                        if(task.isSuccessful()) {
                            User user = task.getResult();
                            if (user != null) {
                                Log.d(TAG, "User: "+user.getName());
                                sessionUserLiveData.setValue(SessionResource.authenticate(user));
                                sessionManager.authenticate(sessionUserLiveData);
                            }
                            else {
                                authManager.signOut();
                                //sessionManager.logOut();
                                Log.w(TAG, "No user data!");
                                toastMessageLiveData.setValue(new TextStringResource("Invalid Session!"));
                            }
                        }
                        else {
                            authManager.signOut();
                            sessionManager.logOut();
                            Log.w(TAG, task.getException().getMessage());
                            toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_unable_to_get_user));
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                toastMessageLiveData.setValue(new TextStringResource(e.getMessage()));
            }
        }
        else {
            sessionManager.logOut();
        }
    }
}
