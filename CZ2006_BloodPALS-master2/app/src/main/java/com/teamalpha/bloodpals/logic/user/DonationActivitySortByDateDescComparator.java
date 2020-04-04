package com.teamalpha.bloodpals.logic.user;

import java.util.Comparator;

public class DonationActivitySortByDateDescComparator implements Comparator<DonationActivity> {
    @Override
    public int compare(DonationActivity o1, DonationActivity o2) {
        return o2.getActivityDateTime().compareTo(o1.getActivityDateTime());
    }
}
