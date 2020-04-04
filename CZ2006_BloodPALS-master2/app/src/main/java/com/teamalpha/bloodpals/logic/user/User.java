package com.teamalpha.bloodpals.logic.user;

import com.teamalpha.bloodpals.logic.auth.AuthUser;

import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class User extends AuthUser {

    private String name;

    @Nullable
    private Date dateOfBirth;

    @Nullable
    private String bloodType;

    private long points;

    private List<String> roles;

    public User(String id, String email, String name, @Nullable Date dateOfBirth, @Nullable String bloodType, long points, List<String> roles) {
        super(id, email);
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.points = points;
        this.roles = roles;
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

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
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

    public boolean hasRole(String roleName) {
        for(String role : roles) {
            if(role.trim().toLowerCase().contains(roleName.trim().toLowerCase()))
                return true;
        }
        return false;
    }

}
