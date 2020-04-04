package com.teamalpha.bloodpals.data.firebase;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class UserDoc {

    private String id;

    private String email;

    private String name;

    @Nullable
    private Date dateOfBirth;

    @Nullable
    private String bloodType;

    private long points;

    private List<String> roles;

    /**
     * Required for Firebase DocumentSnapshot toObject conversion
     */
    public UserDoc() {}

    public UserDoc(String id, String email, String name, @Nullable Date dateOfBirth, @Nullable String bloodType, long points, List<String> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.points = points;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@Nullable Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(@Nullable String bloodType) {
        this.bloodType = bloodType;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
