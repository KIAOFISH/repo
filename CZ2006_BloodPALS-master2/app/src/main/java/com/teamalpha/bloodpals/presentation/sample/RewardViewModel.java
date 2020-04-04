package com.teamalpha.bloodpals.presentation.sample;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

import com.teamalpha.bloodpals.data.firebase.RewardDoc;
import com.teamalpha.bloodpals.logic.reward.Reward;
import com.teamalpha.bloodpals.logic.reward.RewardManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import com.teamalpha.bloodpals.R;

import com.teamalpha.bloodpals.presentation.utils.StringResource;
import com.teamalpha.bloodpals.presentation.utils.TextStringResource;
import com.teamalpha.bloodpals.presentation.utils.SingleLiveEvent;
import com.teamalpha.bloodpals.presentation.utils.IdStringResource;

import javax.inject.Inject;

public class RewardViewModel extends ViewModel {
    private static final String TAG = "RewardViewModel";
    private final MutableLiveData<List<Reward>> RewardLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isInProgressLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<StringResource> toastMessageLiveData = new SingleLiveEvent<>();
    private  RewardManager rewardManager;
    private Object LiveData;

    @Inject
    public RewardViewModel(RewardManager rewardManager) {
        this.rewardManager = rewardManager;

        Log.d(TAG, "RewardViewModel: view model is readRewardViewModel: view model is ready...");
    }
    public LiveData<StringResource> observeToastMessage() {
        return toastMessageLiveData;
    }

    public LiveData<List<Reward>> getRewardLiveData() {

        return RewardLiveData;
    }

    public void loadData() {
        try {

            rewardManager.getRewardById().addOnCompleteListener(new OnCompleteListener<List<Reward>>() {
                @Override
                public void onComplete(@NonNull Task<List<Reward>> task) {

                    if (task.isSuccessful()) {
                        RewardLiveData.setValue(task.getResult());

                           /* Reward reward = (Reward) task.getResult();
                            if (reward == null) {
                                Log.w(TAG, "loadData: ERROR - No reward found");
                                toastMessageLiveData.setValue(new TextStringResource("Invalid scan code!"));
                            }*/

                    } else {
                        Log.e(TAG, "loadData: ERROR - " + task.getException().getMessage());
                        toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_reward_exception));
                        RewardLiveData.setValue(null);
                    }
                    isInProgressLiveData.setValue(false);

                }


            });
        }
        catch (Exception e) {
            Log.e(TAG, "loadData: ERROR - "+e.getMessage());
            toastMessageLiveData.setValue(new IdStringResource(R.string.message_error_get_reward_exception));
            RewardLiveData.setValue(null);

        }


    }





}
