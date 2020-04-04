package com.teamalpha.bloodpals.data.firebase;

import androidx.annotation.Nullable;

import java.util.Date;

public class DonationActivityDoc {

    @Nullable
    private String id;

    private Date activityDateTime;

    private String location;

    @Nullable
    private float donatedAmount;

    /**
     * Required for Firebase DocumentSnapshot toObject conversion
     */
    public DonationActivityDoc() {}

    public DonationActivityDoc(@Nullable String id, Date activityDateTime, String location, @Nullable float donatedAmount) {
        this.id = id;
        this.activityDateTime = activityDateTime;
        this.location = location;
        this.donatedAmount = donatedAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public Date getActivityDateTime() {
        return activityDateTime;
    }

    public void setActivityDateTime(Date activityDateTime) {
        this.activityDateTime = activityDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getDonatedAmount() {
        return donatedAmount;
    }

    public void setDonatedAmount(@Nullable float donatedAmount) {
        this.donatedAmount = donatedAmount;
    }

}
