package com.teamalpha.bloodpals.presentation.mydonationactivities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamalpha.bloodpals.R;
import com.teamalpha.bloodpals.logic.user.DonationActivity;
import com.teamalpha.bloodpals.logic.user.DonationActivitySortByDateAscComparator;
import com.teamalpha.bloodpals.logic.user.DonationActivitySortByDateDescComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class MyDonationActivitiesRecyclerAdapter extends RecyclerView.Adapter<MyDonationActivitiesRecyclerAdapter.MyDonationActivitiesRecyclerViewHolder> implements Filterable {

    private List<DonationActivity> donationActivityListFull;
    private List<DonationActivity> donationActivityList = new ArrayList<>();
    private OnClickListener onClickListener;

    public void setDonationActivityList(List<DonationActivity> donationActivityList){
        this.donationActivityListFull = donationActivityList;
        //this.donationActivityList = new ArrayList<>(donationActivityList);
        this.getFilter().filter(null);
        //notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void sortByDateTimeAscending() {
        donationActivityList.sort(new DonationActivitySortByDateAscComparator());
        notifyDataSetChanged();
    }

    public void sortByDateTimeDescending() {
        donationActivityList.sort(new DonationActivitySortByDateDescComparator());
        notifyDataSetChanged();
    }

    public DonationActivity getDonationActivity(int index) {
        return donationActivityList.get(index);
    }

    @NonNull
    @Override
    public MyDonationActivitiesRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_donation_activity_list_item, parent, false);
        return new MyDonationActivitiesRecyclerAdapter.MyDonationActivitiesRecyclerViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyDonationActivitiesRecyclerViewHolder holder, int position) {
        DonationActivity donationActivity = donationActivityList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        holder.textDateTime.setText(dateFormat.format(donationActivity.getActivityDateTime()));

        holder.textLocation.setText(donationActivity.getLocation());

        holder.textDonatedAmount.setText(String.valueOf(donationActivity.getDonatedAmount()));
    }

    @Override
    public int getItemCount() {
        if(donationActivityList != null)
            return donationActivityList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return filterDonationActivityList;
    }

    private Filter filterDonationActivityList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DonationActivity> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(donationActivityListFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DonationActivity donationActivity : donationActivityListFull) {
                    if(donationActivity.getLocation().toLowerCase().contains(filterPattern)) {
                        filteredList.add(donationActivity);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            donationActivityList.clear();
            donationActivityList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyDonationActivitiesRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textDateTime;
        private TextView textLocation;
        private TextView textDonatedAmount;
        private OnClickListener onClickListener;

        public MyDonationActivitiesRecyclerViewHolder(@NonNull View view, OnClickListener onClickListener) {
            super(view);
            this.onClickListener = onClickListener;

            textDateTime = view.findViewById(R.id.text_date_time);
            textLocation = view.findViewById(R.id.text_location);
            textDonatedAmount = view.findViewById(R.id.text_donated_amount);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }
}
