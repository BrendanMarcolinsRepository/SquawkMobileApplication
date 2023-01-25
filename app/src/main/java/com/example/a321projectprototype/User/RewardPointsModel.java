package com.example.a321projectprototype.User;

public class RewardPointsModel {
    private String bird_status;
    private int reward_points;


    RewardPointsModel() { }
    RewardPointsModel(String bird_status, int reward_points) {
        this.bird_status = bird_status;
        this.reward_points = reward_points;
    }

    public String getBird_status() {
        return bird_status;
    }

    public void setBird_status(String bird_status) {
        this.bird_status = bird_status;
    }

    public int getReward_points() {
        return reward_points;
    }

    public void setReward_points(int reward_points) {
        this.reward_points = reward_points;
    }
}
