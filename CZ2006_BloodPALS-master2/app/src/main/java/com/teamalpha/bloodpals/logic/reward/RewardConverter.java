package com.teamalpha.bloodpals.logic.reward;
import com.teamalpha.bloodpals.data.firebase.RewardDoc;


public class RewardConverter {

    public static Reward convertRewardDocToReward(RewardDoc rewardDoc) {
        Reward reward = new Reward(
                rewardDoc.getId(),
                rewardDoc.getName(),
                rewardDoc.getPoints(),
                rewardDoc.getDescription()


        );
        return reward;
    }

    public static RewardDoc convertRewardToRewardDoc(Reward reward) {
        RewardDoc rewardDoc = new RewardDoc(
                reward.getId(),
                reward.getName(),
                reward.getPoints(),
                reward.getDescription()
        );
        return rewardDoc;
    }

}

