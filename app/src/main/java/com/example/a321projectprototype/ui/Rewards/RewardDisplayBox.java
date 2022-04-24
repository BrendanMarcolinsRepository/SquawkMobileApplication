package com.example.a321projectprototype.ui.Rewards;

import com.google.firebase.Timestamp;

public class RewardDisplayBox {
    private String birdName;
    private Timestamp timestamp;

    RewardDisplayBox(String birdName, Timestamp timestamp) {
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
