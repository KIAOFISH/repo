package com.teamalpha.bloodpals.data.bloodbanks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("_id")
    @Expose
    private Integer id;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("donation_type")
    @Expose
    private String donationType;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("postal_code")
    @Expose
    private String postalCode;

    @SerializedName("monday_operating_hour")
    @Expose
    private String mondayOperatingHour;

    @SerializedName("tuesday_operating_hour")
    @Expose
    private String tuesdayOperatingHour;

    @SerializedName("wednesday_operating_hour")
    @Expose
    private String wednesdayOperatingHour;

    @SerializedName("thursday_operating_hour")
    @Expose
    private String thursdayOperatingHour;

    @SerializedName("friday_operating_hour")
    @Expose
    private String fridayOperatingHour;

    @SerializedName("saturday_operating_hour")
    @Expose
    private String saturdayOperatingHour;

    @SerializedName("sunday_operating_hour")
    @Expose
    private String sundayOperatingHour;

    @SerializedName("eve_of_major_public_holiday_operating_hour")
    @Expose
    private String eveOfMajorPublicHolidayOperatingHour;

    @SerializedName("public_holiday_operating_hour")
    @Expose
    private String publicHolidayOperatingHour;

    public Record(Integer id,
                  String location,
                  String donationType,
                  String address,
                  String postalCode,
                  String mondayOperatingHour,
                  String tuesdayOperatingHour,
                  String wednesdayOperatingHour,
                  String thursdayOperatingHour,
                  String fridayOperatingHour,
                  String saturdayOperatingHour,
                  String sundayOperatingHour,
                  String eveOfMajorPublicHolidayOperatingHour,
                  String publicHolidayOperatingHour) {
        this.id = id;
        this.location = location;
        this.donationType = donationType;
        this.address = address;
        this.postalCode = postalCode;
        this.mondayOperatingHour = mondayOperatingHour;
        this.tuesdayOperatingHour = tuesdayOperatingHour;
        this.wednesdayOperatingHour = wednesdayOperatingHour;
        this.thursdayOperatingHour = thursdayOperatingHour;
        this.fridayOperatingHour = fridayOperatingHour;
        this.saturdayOperatingHour = saturdayOperatingHour;
        this.sundayOperatingHour = sundayOperatingHour;
        this.eveOfMajorPublicHolidayOperatingHour = eveOfMajorPublicHolidayOperatingHour;
        this.publicHolidayOperatingHour = publicHolidayOperatingHour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMondayOperatingHour() {
        return mondayOperatingHour;
    }

    public void setMondayOperatingHour(String mondayOperatingHour) {
        this.mondayOperatingHour = mondayOperatingHour;
    }

    public String getTuesdayOperatingHour() {
        return tuesdayOperatingHour;
    }

    public void setTuesdayOperatingHour(String tuesdayOperatingHour) {
        this.tuesdayOperatingHour = tuesdayOperatingHour;
    }

    public String getWednesdayOperatingHour() {
        return wednesdayOperatingHour;
    }

    public void setWednesdayOperatingHour(String wednesdayOperatingHour) {
        this.wednesdayOperatingHour = wednesdayOperatingHour;
    }

    public String getThursdayOperatingHour() {
        return thursdayOperatingHour;
    }

    public void setThursdayOperatingHour(String thursdayOperatingHour) {
        this.thursdayOperatingHour = thursdayOperatingHour;
    }

    public String getFridayOperatingHour() {
        return fridayOperatingHour;
    }

    public void setFridayOperatingHour(String fridayOperatingHour) {
        this.fridayOperatingHour = fridayOperatingHour;
    }

    public String getSaturdayOperatingHour() {
        return saturdayOperatingHour;
    }

    public void setSaturdayOperatingHour(String saturdayOperatingHour) {
        this.saturdayOperatingHour = saturdayOperatingHour;
    }

    public String getSundayOperatingHour() {
        return sundayOperatingHour;
    }

    public void setSundayOperatingHour(String sundayOperatingHour) {
        this.sundayOperatingHour = sundayOperatingHour;
    }

    public String getEveOfMajorPublicHolidayOperatingHour() {
        return eveOfMajorPublicHolidayOperatingHour;
    }

    public void setEveOfMajorPublicHolidayOperatingHour(String eveOfMajorPublicHolidayOperatingHour) {
        this.eveOfMajorPublicHolidayOperatingHour = eveOfMajorPublicHolidayOperatingHour;
    }

    public String getPublicHolidayOperatingHour() {
        return publicHolidayOperatingHour;
    }

    public void setPublicHolidayOperatingHour(String publicHolidayOperatingHour) {
        this.publicHolidayOperatingHour = publicHolidayOperatingHour;
    }
}
