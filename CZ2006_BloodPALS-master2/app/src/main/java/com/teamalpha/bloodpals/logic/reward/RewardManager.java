package com.teamalpha.bloodpals.logic.reward;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.teamalpha.bloodpals.data.firebase.RewardFirestoreService;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.teamalpha.bloodpals.data.firebase.RewardDoc;
import com.teamalpha.bloodpals.data.firebase.RewardFirestoreService;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
public class RewardManager {

    private RewardFirestoreService rewardFirestoreService;


    @Inject
    public RewardManager(RewardFirestoreService rewardFirestoreService) {
        this.rewardFirestoreService = rewardFirestoreService;
    }


    public Task<List<Reward>> getRewardById() throws Exception {
        return rewardFirestoreService.getRewardById().continueWith(new Continuation<List<RewardDoc>, List<Reward>>() {
            @Override
            public List<Reward> then(@NonNull Task<List<RewardDoc>> task) throws Exception {
                if (task.isSuccessful()) {
                    List<Reward> rewardsList = new ArrayList<Reward>();

                    for(RewardDoc rewardDoc : task.getResult()) {
                        Reward reward = RewardConverter.convertRewardDocToReward(rewardDoc);
                        rewardsList.add(reward);
                    }
                    return rewardsList;
                }

                throw new Exception(task.getException().getMessage());
            }
        });
    }

    public Task setReward (Reward reward){
        return rewardFirestoreService.setReward(RewardConverter.convertRewardToRewardDoc(reward));
    }
}
