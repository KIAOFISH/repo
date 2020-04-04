package com.teamalpha.bloodpals.data.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

public class FirebaseAuthService {

    private static final String TAG = "FirebaseAuthService";

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;

    private FirebaseAuthService.AuthStateListener authStateListener;

    @Inject
    public FirebaseAuthService() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public final void onAuthStateChanged(final @NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser _user = firebaseAuth.getCurrentUser();

                if (_user != null) {
                    // User is signed in
                    user = _user;
                } else {
                    // User is signed out
                    user = null;
                }

                if (authStateListener != null) authStateListener.onAuthorizedChanged(_user != null);
            }
        };
    }

    public final void subscribe() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public final void unSubscribe() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public final void setAuthStateListener(final FirebaseAuthService.AuthStateListener _authStateListener) {
        authStateListener = _authStateListener;
    }

    @Nullable
    public final FirebaseUser getUser() {
        return user;
    }

    public final void signOut() {
        mAuth.signOut();
    }

    public final Task<AuthResult> createUserWithEmailAndPassword(final @NonNull String email, final @NonNull String password) {
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public final Task<AuthResult> signInWithEmailAndPassword(final @NonNull String email, final @NonNull String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public interface AuthStateListener {
        void onAuthorizedChanged(boolean isAuthorized);
    }
}
