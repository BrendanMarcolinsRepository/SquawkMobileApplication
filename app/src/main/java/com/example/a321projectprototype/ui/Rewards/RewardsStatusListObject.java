package com.example.a321projectprototype.ui.Rewards;

import com.google.firebase.Timestamp;
//Code by Rui Cao
public class RewardsStatusListObject {
    private String birdName;
    private Timestamp timestamp;

    RewardsStatusListObject(String birdName, Timestamp timestamp) {
        this.birdName = birdName;
        this.timestamp = timestamp;
    }

    public String getBirdName() {
        return birdName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}