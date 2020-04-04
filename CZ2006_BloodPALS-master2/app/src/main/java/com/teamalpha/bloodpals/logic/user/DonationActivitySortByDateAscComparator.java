package com.teamalpha.bloodpals.logic.user;

import java.util.Comparator;

public class DonationActivitySortByDateAscComparator implements Comparator<DonationActivity> {
    @Override
    public int compare(DonationActivity o1, DonationActivity o2) {
        return o1.getActivityDateTime().compareTo(o2.getActivityDateTime());
    }
}
