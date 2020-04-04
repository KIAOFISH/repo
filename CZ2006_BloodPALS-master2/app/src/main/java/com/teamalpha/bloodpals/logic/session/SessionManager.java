package com.teamalpha.bloodpals.logic.session;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.teamalpha.bloodpals.logic.user.User;

import javax.inject.Inject;

public class SessionManager {

    private static final String TAG = "SessionManager";

    private MediatorLiveData<SessionResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager() {
    }

    public void authenticate(final LiveData<SessionResource<User>> source){
        if(cachedUser != null){
            cachedUser.setValue(SessionResource.loading((User)null));
            cachedUser.addSource(source, new Observer<SessionResource<User>>() {
                @Override
                public void onChanged(SessionResource<User> userSessionResource) {
                    cachedUser.setValue(userSessionResource);
                    cachedUser.removeSource(source);
                }
            });
        }
        else {
            Log.d(TAG, "authenticate: previous session detected. Retrieving user from cache.");
        }
    }

    public void update(User user) {
        cachedUser.getValue().setData(user);
    }

    public void logOut(){
        Log.d(TAG, "logOut: logging out...");
        cachedUser.setValue(SessionResource.<User>logout());
    }

    public LiveData<SessionResource<User>> getUser(){
        return cachedUser;
    }

}
