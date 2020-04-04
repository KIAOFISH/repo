package com.teamalpha.bloodpals.presentation.sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamalpha.bloodpals.R;

import com.teamalpha.bloodpals.R.layout;
import com.teamalpha.bloodpals.data.firebase.RewardDoc;
import com.teamalpha.bloodpals.logic.reward.Reward;
import com.teamalpha.bloodpals.logic.reward.RewardManager;

import java.util.ArrayList;
import java.util.List;

public class RewardRecyclerAdapter extends RecyclerView.Adapter<RewardRecyclerAdapter.RewardRecyclerViewHolder> {

    private List<Reward> RewardList = new ArrayList<>();

    @NonNull
    @Override
    public RewardRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout.reward_list_items, parent, false);
        return new RewardRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardRecyclerViewHolder holder, int position) {
        Reward reward = RewardList.get(position);
        holder.relname.setText(reward.getName());
        holder.relpoint.setText(reward.getPoints());
        holder.reldescription.setText(reward.getDescription());
    }

    @Override
    public int getItemCount() {
        if (RewardList == null)
            return 0;
        else
            return  RewardList.size();
    }

    public void setRewardList(List<Reward> RewardList){
        this.RewardList = RewardList;
        notifyDataSetChanged();
    }

    public class RewardRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView relname;
        private TextView relpoint;
        private TextView reldescription;

        public RewardRecyclerViewHolder(@NonNull View view) {
            super(view);
            relname = view.findViewById(R.id.Text_name);
            relpoint = view.findViewById(R.id.Text_point);
            reldescription=view.findViewById(R.id.Text_description);

        }
    }

}
