package com.teamalpha.bloodpals.data.firebase;

import java.util.Date;

public class UserPointsTransactionDoc {

    private int points;

    private Date transactionOn;

    private String transactionById;

    private String transactionByName;

    private String transactionByEmail;

    public UserPointsTransactionDoc(int points, Date transactionOn, String transactionById, String transactionByName, String transactionByEmail) {
        this.points = points;
        this.transactionOn = transactionOn;
        this.transactionById = transactionById;
        this.transactionByName = transactionByName;
        this.transactionByEmail = transactionByEmail;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getTransactionOn() {
        return transactionOn;
    }

    public void setTransactionOn(Date transactionOn) {
        this.transactionOn = transactionOn;
    }

    public String getTransactionById() {
        return transactionById;
    }

    public void setTransactionById(String transactionById) {
        this.transactionById = transactionById;
    }

    public String getTransactionByName() {
        return transactionByName;
    }

    public void setTransactionByName(String transactionByName) {
        this.transactionByName = transactionByName;
    }

    public String getTransactionByEmail() {
        return transactionByEmail;
    }

    public void setTransactionByEmail(String transactionByEmail) {
        this.transactionByEmail = transactionByEmail;
    }

}
