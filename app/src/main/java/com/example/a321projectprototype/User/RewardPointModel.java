package com.example.a321projectprototype.User;

public class RewardPointModel
{
    private String birdName;
    private String birdStatus;
    private int birdCount;
    private int rewardPoints;
    private int totalRewardPoints;

    public RewardPointModel(){}

    public RewardPointModel(String birdName, String birdStatus, int birdCount, int rewardPoints, int totalRewardPoints) {
        this.birdName = birdName;
        this.birdStatus = birdStatus;
        this.birdCount = birdCount;
        this.rewardPoints = rewardPoints;
        this.totalRewardPoints = totalRewardPoints;
    }

    public String getBirdName() {
        return birdName;
    }

    public void setBirdName(String birdName) {
        this.birdName = birdName;
    }

    public String getBirdStatus() {
        return birdStatus;
    }

    public void setBirdStatus(String birdStatus) {
        this.birdStatus = birdStatus;
    }

    public int getBirdCount() {
        return birdCount;
    }

    public void setBirdCount(int birdCount) {
        this.birdCount = birdCount;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public int getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalRewardPoints(int totalRewardPoints) {
        this.totalRewardPoints = totalRewardPoints;
    }
}
