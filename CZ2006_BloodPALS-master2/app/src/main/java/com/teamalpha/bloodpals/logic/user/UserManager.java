package com.teamalpha.bloodpals.logic.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.data.firebase.DonationActivityDoc;
import com.teamalpha.bloodpals.data.firebase.UserDoc;
import com.teamalpha.bloodpals.data.firebase.UserFirestoreService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserManager {

    private UserFirestoreService userFirestoreService;

    @Inject
    public UserManager(UserFirestoreService userFirestoreService) {
        this.userFirestoreService = userFirestoreService;
    }

    public Task<User> getUserById(String userId) throws Exception {
        return userFirestoreService.getUserById(userId).continueWith(new Continuation<UserDoc, User>() {
            @Override
            public User then(@NonNull Task<UserDoc> task) throws Exception {
                if(task.isSuccessful()) {
                    if(task.getResult() != null)
                        return UserConverter.convertUserDocToUser(task.getResult());
                    else
                        return null;
                }
                throw new Exception(task.getException().getMessage());
            }
        });
    }

    public Task setUser(String userId, User user) {
        return userFirestoreService.setUser(userId, UserConverter.convertUserToUserDoc(user));
    }

    public Task<Boolean> updateUserProfile(String userId, User user) {
        return userFirestoreService.updateUserProfile(userId, UserConverter.convertUserToUserDoc(user));
    }

    public Task<Long> addUserPoints(String userId, int pointsToAdd, String transactionById, String transactionByName, String transactionByEmail) {
        return userFirestoreService.addUserPoints(userId, pointsToAdd, transactionById, transactionByName, transactionByEmail);
    }

    public Task<Long> deductUserPoints(String userId, int pointsToDeduct, String transactionById, String transactionByName, String transactionByEmail) {
        return  userFirestoreService.deductUserPoints(userId, pointsToDeduct, transactionById, transactionByName, transactionByEmail);
    }

    public Task<List<DonationActivity>> getUserDonationActivities(String userId) {
        return userFirestoreService.getUserDonationActivities(userId).continueWith(new Continuation<List<DonationActivityDoc>, List<DonationActivity>>() {
            @Override
            public List<DonationActivity> then(@NonNull Task<List<DonationActivityDoc>> task) throws Exception {
                if(task.isSuccessful()) {
                    List<DonationActivity>  donationActivities = new ArrayList<DonationActivity>();
                    for(DonationActivityDoc donationActivityDoc : task.getResult()) {
                        donationActivities.add(DonationActivityConverter.convertDonationActivityDocToDonationActivity(donationActivityDoc));
                    }
                    return donationActivities;
                }
                throw new Exception(task.getException().getMessage());
            }
        });
    }

    public Task<DonationActivity> getUserDonationActivity(String userId, String donationActivityId) {
        return userFirestoreService.getUserDonationActivity(userId, donationActivityId).continueWith(new Continuation<DonationActivityDoc, DonationActivity>() {
            @Override
            public DonationActivity then(@NonNull Task<DonationActivityDoc> task) throws Exception {
                if(task.isSuccessful()) {
                    if(task.getResult() != null)
                        return DonationActivityConverter.convertDonationActivityDocToDonationActivity(task.getResult());
                    else
                        return null;
                }
                throw new Exception(task.getException().getMessage());
            }
        });
    }

    public Task<Boolean> addDonationActivity(String userId, DonationActivity donationActivity) {
        return userFirestoreService.addDonationActivity(userId, DonationActivityConverter.convertDonationActivityToDonationActivityDoc(donationActivity));
    }

    public Task<Boolean> updateDonationActivity(String userId, DonationActivity donationActivity) {
        return userFirestoreService.updateDonationActivity(userId, DonationActivityConverter.convertDonationActivityToDonationActivityDoc(donationActivity));
    }

    public Task<Boolean> deleteDonationActivity(String userId, String donationActivityId) {
        return userFirestoreService.deleteDonationActivity(userId, donationActivityId);
    }

}
