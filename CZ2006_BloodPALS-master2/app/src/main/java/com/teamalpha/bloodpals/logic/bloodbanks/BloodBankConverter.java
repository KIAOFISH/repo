package com.teamalpha.bloodpals.logic.bloodbanks;

import com.teamalpha.bloodpals.data.bloodbanks.Record;

import java.util.ArrayList;
import java.util.List;

public class BloodBankConverter {

    public static List<BloodBank> convertRecordToBloodBank (List<Record> record)
    {
        List<BloodBank> bloodBankList = new ArrayList<>();
        for(int i = 0; i<record.size(); i++){
            bloodBankList.add(new BloodBank(
                    record.get(i).getId(),
                    record.get(i).getLocation(),
                    record.get(i).getDonationType(),
                    record.get(i).getAddress(),
                    record.get(i).getPostalCode(),
                    record.get(i).getMondayOperatingHour(),
                    record.get(i).getTuesdayOperatingHour(),
                    record.get(i).getWednesdayOperatingHour(),
                    record.get(i).getThursdayOperatingHour(),
                    record.get(i).getFridayOperatingHour(),
                    record.get(i).getSaturdayOperatingHour(),
                    record.get(i).getSundayOperatingHour(),
                    record.get(i).getEveOfMajorPublicHolidayOperatingHour(),
                    record.get(i).getPublicHolidayOperatingHour()
            ));

        }
        return bloodBankList;
    }
}
