package com.teamalpha.bloodpals.logic.user;

import com.teamalpha.bloodpals.data.firebase.UserDoc;

public class UserConverter {

    public static User convertUserDocToUser(UserDoc userDoc) {
        User user = new User(
                userDoc.getId(),
                userDoc.getEmail(),
                userDoc.getName(),
                userDoc.getDateOfBirth(),
                userDoc.getBloodType(),
                userDoc.getPoints(),
                userDoc.getRoles()
        );
        return user;
    }

    public static UserDoc convertUserToUserDoc(User user) {
        UserDoc userDoc = new UserDoc(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getDateOfBirth(),
                user.getBloodType(),
                user.getPoints(),
                user.getRoles()
        );
        return userDoc;
    }

}
