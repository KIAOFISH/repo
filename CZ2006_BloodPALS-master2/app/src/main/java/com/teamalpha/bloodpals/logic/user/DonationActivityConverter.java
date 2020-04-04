package com.teamalpha.bloodpals.logic.user;

import com.teamalpha.bloodpals.data.firebase.DonationActivityDoc;

public class DonationActivityConverter {

    public static DonationActivity convertDonationActivityDocToDonationActivity(DonationActivityDoc donationActivityDoc) {
        return new DonationActivity(
                donationActivityDoc.getId(),
                donationActivityDoc.getActivityDateTime(),
                donationActivityDoc.getLocation(),
                donationActivityDoc.getDonatedAmount()
        );
    }

    public static DonationActivityDoc convertDonationActivityToDonationActivityDoc(DonationActivity donationActivity) {
        return new DonationActivityDoc(
                donationActivity.getId(),
                donationActivity.getActivityDateTime(),
                donationActivity.getLocation(),
                donationActivity.getDonatedAmount()
        );
    }

}
