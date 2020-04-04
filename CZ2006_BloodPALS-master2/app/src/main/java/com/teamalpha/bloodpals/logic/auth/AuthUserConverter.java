package com.teamalpha.bloodpals.logic.auth;

import com.google.firebase.auth.FirebaseUser;

public class AuthUserConverter {

    public static AuthUser convertUser(FirebaseUser firebaseUser) {
        return new AuthUser(firebaseUser.getUid(), firebaseUser.getEmail());
    }

}
