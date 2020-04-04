package com.teamalpha.bloodpals.presentation.bloodbanks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.data.bloodbanks.Record;

import java.util.ArrayList;
import java.util.List;

public class BloodBankRecyclerAdapter extends RecyclerView.Adapter<BloodBankRecyclerAdapter.BloodBankRecyclerViewHolder> {

    private List<Record> bloodBankList = new ArrayList<>();;

    @NonNull
    @Override
    public BloodBankRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodbank_list_item, parent, false);
        return new BloodBankRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodBankRecyclerViewHolder holder, int position) {
        holder.textLocation.setText(bloodBankList.get(position).getLocation());
        holder.textAddress.setText(bloodBankList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }

    public void setBloodBankList(List<Record> bloodBankList){
        this.bloodBankList = bloodBankList;
        notifyDataSetChanged();
    }

    public class BloodBankRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView textLocation;
        private TextView textAddress;

        public BloodBankRecyclerViewHolder(@NonNull View view) {
            super(view);
            textLocation = view.findViewById(R.id.text_location);
            textAddress = view.findViewById(R.id.text_address);
        }
    }

}
