package com.teamalpha.bloodpals.logic.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.teamalpha.bloodpals.data.firebase.FirebaseAuthService;

import javax.inject.Inject;

public class AuthManager implements  FirebaseAuthService.AuthStateListener {

    private static final String TAG = "AuthManager";

    private FirebaseAuthService firebaseAuthService;

    private AuthManager.AuthStateListener authStateListener;

    @Inject
    public AuthManager(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
        this.firebaseAuthService.setAuthStateListener(this);
    }

    public void subscribe() {
        firebaseAuthService.subscribe();
    }

    public void unSubscribe() {
        firebaseAuthService.unSubscribe();
    }

    public void setAuthStateListener(AuthManager.AuthStateListener _authStateListener) {
        authStateListener = _authStateListener;
    }

    @Nullable
    public AuthUser getUser() {
        return AuthUserConverter.convertUser(firebaseAuthService.getUser());
    }

    public void signOut() {
        firebaseAuthService.signOut();
    }

    public void login(@NonNull String email, @NonNull String password, @Nullable OnCompleteListener<AuthUser> onCompleteListener) {
        Task<AuthUser> resultTask = firebaseAuthService.signInWithEmailAndPassword(email, password).continueWith(new Continuation<AuthResult, AuthUser>() {
            @Override
            public AuthUser then(@NonNull Task<AuthResult> task) throws Exception {
                if(task.isSuccessful()) {
                    return AuthUserConverter.convertUser(task.getResult().getUser());
                }
                throw new Exception(task.getException().getMessage());
            }
        });

        if (onCompleteListener != null) {
            resultTask.addOnCompleteListener(onCompleteListener);
        }
    }

    public void register(@NonNull String email, @NonNull String password, @Nullable OnCompleteListener<AuthUser> onCompleteListener) {
        Task<AuthUser> resultTask = firebaseAuthService.createUserWithEmailAndPassword(email, password).continueWith(new Continuation<AuthResult, AuthUser>() {
            @Override
            public AuthUser then(@NonNull Task<AuthResult> task) throws Exception {
                if(task.isSuccessful()) {
                    return AuthUserConverter.convertUser(task.getResult().getUser());
                }
                throw new Exception(task.getException().getMessage());
            }
        });

        if (onCompleteListener != null) {
            resultTask.addOnCompleteListener(onCompleteListener);
        }
    }

    @Override
    public void onAuthorizedChanged(boolean isAuthorized) {
        if (authStateListener != null) authStateListener.onAuthStateChanged(isAuthorized);
    }

    public interface AuthStateListener {
        void onAuthStateChanged(boolean isAuthorized);
    }

}
