package com.teamalpha.bloodpals.di;

import com.teamalpha.bloodpals.data.firebase.FirebaseAuthService;
import com.teamalpha.bloodpals.data.firebase.UserFirestoreService;
import com.teamalpha.bloodpals.logic.auth.AuthManager;
import com.teamalpha.bloodpals.logic.session.SessionManager;
import com.teamalpha.bloodpals.logic.user.UserManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    public FirebaseAuthService getFirebaseAuthService() {
        return new FirebaseAuthService();
    }

    @Provides
    @Singleton
    public AuthManager getAuthManager(FirebaseAuthService firebaseAuthService) {
        return new AuthManager(firebaseAuthService);
    }

    @Provides
    @Singleton
    public UserFirestoreService getUserFirestoreService() {
        return new UserFirestoreService();
    }

    @Provides
    @Singleton
    public UserManager getUserManager(UserFirestoreService userFirestoreService) {
        return new UserManager(userFirestoreService);
    }

    @Provides
    @Singleton
    public SessionManager getSessionManager() {
        return new SessionManager();
    }

}
