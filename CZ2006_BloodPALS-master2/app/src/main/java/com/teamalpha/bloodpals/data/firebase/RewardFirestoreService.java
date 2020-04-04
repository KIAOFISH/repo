package com.teamalpha.bloodpals.data.firebase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamalpha.bloodpals.logic.reward.Reward;
import com.google.firebase.firestore.CollectionReference;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import java.util.List;
import com.teamalpha.bloodpals.data.firebase.RewardDoc;


import java.util.ArrayList;

import javax.inject.Inject;

public class RewardFirestoreService {

    private final String TAG = "FirebaseFirestoreService";

    private final String REWARDS_COLLECTION_ID = "Rewards";

    private FirebaseFirestore db;

    private CollectionReference collectionRef;

    @Inject
    public RewardFirestoreService() {
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection(REWARDS_COLLECTION_ID);
    }

    /*public void Reward_Quary(){

        mFirestore.collection("Rewards").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshot) {
                    for(QueryDocumentSnapshot documentsnapshot: queryDocumentSnapshot){
                        Reward reward =documentsnapshot.toObject(Reward.class);

                    }*/


    public Task<List<RewardDoc>> getRewardById() throws Exception {
        return collectionRef.get().continueWith(new Continuation<QuerySnapshot, List<RewardDoc>>() {
            @Override
            public List<RewardDoc> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                if (task.isSuccessful()) {
                    List<RewardDoc> rewardsList = new ArrayList<RewardDoc>();
                    for (QueryDocumentSnapshot documentsnapshot : task.getResult()) {
                        rewardsList.add( documentsnapshot.toObject(RewardDoc.class));


                    }
                    return rewardsList;


                }
                throw new Exception(task.getException().getMessage());
            }
        });
    }

    public Task setReward ( RewardDoc rewardDoc){
        return collectionRef.document().set(rewardDoc);
    }
}








