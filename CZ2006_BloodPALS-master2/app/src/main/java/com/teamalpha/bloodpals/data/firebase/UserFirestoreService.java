package com.teamalpha.bloodpals.data.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class UserFirestoreService {

    private final String TAG = "UserFirestoreService";

    private final String USER_COLLECTION_ID = "Users";
    private final String USER_POINTS_TRANSACTION_COLLECTION_ID = "PointsTransactions";
    private final String USER_DONATION_ACTIVITY_COLLECTION_ID = "DonationActivities";

    private FirebaseFirestore db;

    private CollectionReference collectionRef;

    @Inject
    public UserFirestoreService() {
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection(USER_COLLECTION_ID);
    }

    public Task<UserDoc> getUserById(String userId) {
        return collectionRef.document(userId).get().continueWith(new Continuation<DocumentSnapshot, UserDoc>() {
            @Override
            public UserDoc then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        return document.toObject(UserDoc.class);
                    } else {
                        // No such document
                        return null;
                    }
                }
                throw new Exception("Oops! An error occurred when trying getting user from database. Please try again.");
            }
        });
    }

    public Task setUser(String userId, UserDoc userDoc) {
        return collectionRef.document(userId).set(userDoc);
    }

    public Task<Boolean> updateUserProfile(String userId, UserDoc userDoc) {
        return collectionRef.document(userId)
                .update("name", userDoc.getName(),
                        "dateOfBirth", userDoc.getDateOfBirth(),
                        "bloodType", userDoc.getBloodType()).continueWith(new Continuation<Void, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<Void> task) throws Exception {
                        if(task.isSuccessful())
                            return true;
                        return false;
                    }
                });
    }

    public Task<Long> addUserPoints(String userId, final int pointsToAdd, final String transactionById, final String transactionByName, final String transactionByEmail) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final DocumentReference newUserPointsTransactionDocumentRef = userDocumentRef.collection(USER_POINTS_TRANSACTION_COLLECTION_ID).document();
        return  db.runTransaction(new Transaction.Function<Long>() {
            @Nullable
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot userSnapshot = transaction.get(userDocumentRef);
                if(userSnapshot == null)
                    throw new FirebaseFirestoreException("Invalid user!", FirebaseFirestoreException.Code.ABORTED);

                long newUserPoints = userSnapshot.getLong("points") + pointsToAdd;

                transaction.update(userDocumentRef, "points", newUserPoints);

                UserPointsTransactionDoc userPointsTransactionDoc = new UserPointsTransactionDoc(
                        pointsToAdd,
                        new Date(),
                        transactionById,
                        transactionByName,
                        transactionByEmail
                );

                transaction.set(newUserPointsTransactionDocumentRef, userPointsTransactionDoc);

                return newUserPoints;
            }
        });
    }

    public Task<Long> deductUserPoints(String userId, final int pointsToDeduct, final String transactionById, final String transactionByName, final String transactionByEmail) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final DocumentReference newUserPointsTransactionDocumentRef = userDocumentRef.collection(USER_POINTS_TRANSACTION_COLLECTION_ID).document();
        return  db.runTransaction(new Transaction.Function<Long>() {
            @Nullable
            @Override
            public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot userSnapshot = transaction.get(userDocumentRef);
                if(userSnapshot == null)
                    throw new FirebaseFirestoreException("Invalid user!", FirebaseFirestoreException.Code.ABORTED);

                long newUserPoints = userSnapshot.getLong("points") - pointsToDeduct;
                if(newUserPoints < 0)
                    throw new FirebaseFirestoreException("User points insufficient!", FirebaseFirestoreException.Code.ABORTED);

                transaction.update(userDocumentRef, "points", newUserPoints);

                UserPointsTransactionDoc userPointsTransactionDoc = new UserPointsTransactionDoc(
                        pointsToDeduct * -1,
                        new Date(),
                        transactionById,
                        transactionByName,
                        transactionByEmail
                );

                transaction.set(newUserPointsTransactionDocumentRef, userPointsTransactionDoc);

                return newUserPoints;
            }
        });
    }

    public Task<List<DonationActivityDoc>> getUserDonationActivities(String userId) {
        final CollectionReference userDonationActivityCollectionRef = collectionRef.document(userId).collection(USER_DONATION_ACTIVITY_COLLECTION_ID);
        return userDonationActivityCollectionRef.orderBy("activityDateTime", Query.Direction.DESCENDING).get().continueWith(new Continuation<QuerySnapshot, List<DonationActivityDoc>>() {
            @Override
            public List<DonationActivityDoc> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                if(task.isSuccessful()) {
                    List<DonationActivityDoc> donationActivityDocs = new ArrayList<DonationActivityDoc>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DonationActivityDoc donationActivityDoc = document.toObject(DonationActivityDoc.class);
                        donationActivityDoc.setId(document.getId());
                        donationActivityDocs.add(donationActivityDoc);
                    }
                    return donationActivityDocs;
                }
                throw new Exception("Oops! An error occurred when trying getting donation activities. Please try again.");
            }
        });
    }

    public Task<DonationActivityDoc> getUserDonationActivity(String userId, String donationActivityId) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final DocumentReference userDonationActivityDocumentRef = userDocumentRef.collection(USER_DONATION_ACTIVITY_COLLECTION_ID).document(donationActivityId);
        return userDonationActivityDocumentRef.get().continueWith(new Continuation<DocumentSnapshot, DonationActivityDoc>() {
            @Override
            public DonationActivityDoc then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        return document.toObject(DonationActivityDoc.class);
                    } else {
                        // No such document
                        return null;
                    }
                }
                throw new Exception("Oops! An error occurred when trying getting donation activity from database. Please try again.");
            }
        });
    }

    public Task<Boolean> addDonationActivity(String userId, DonationActivityDoc donationActivityDoc) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final CollectionReference userDonationActivityCollectionRef = userDocumentRef.collection(USER_DONATION_ACTIVITY_COLLECTION_ID);
        return userDonationActivityCollectionRef.add(donationActivityDoc).continueWith(new Continuation<DocumentReference, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<DocumentReference> task) throws Exception {
                if(task.isSuccessful())
                    return true;
                return false;
            }
        });
    }

    public Task<Boolean> updateDonationActivity(String userId, DonationActivityDoc donationActivityDoc) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final CollectionReference userDonationActivityCollectionRef = userDocumentRef.collection(USER_DONATION_ACTIVITY_COLLECTION_ID);
        return userDonationActivityCollectionRef.document(donationActivityDoc.getId())
                .update("id", donationActivityDoc.getId(),
                        "activityDateTime", donationActivityDoc.getActivityDateTime(),
                        "location", donationActivityDoc.getLocation(),
                        "donatedAmount", donationActivityDoc.getDonatedAmount()).continueWith(new Continuation<Void, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<Void> task) throws Exception {
                        if(task.isSuccessful())
                            return true;
                        return false;
                    }
                });
    }

    public Task<Boolean> deleteDonationActivity(String userId, String donationActivityId) {
        final DocumentReference userDocumentRef = collectionRef.document(userId);
        final CollectionReference userDonationActivityCollectionRef = userDocumentRef.collection(USER_DONATION_ACTIVITY_COLLECTION_ID);
        return userDonationActivityCollectionRef.document(donationActivityId).delete().continueWith(new Continuation<Void, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<Void> task) throws Exception {
                if(task.isSuccessful())
                    return true;
                return false;
            }
        });
    }

}
