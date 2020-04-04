package com.teamalpha.bloodpals.data.firebase;

public class RewardDoc {

    private String id;

    private String name;

    private String description;

    private String points;

    public RewardDoc(){

    }

    public RewardDoc(String id, String name, String points, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.points = points;
    }

    /*private boolean isAvailable;*/


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
