package com.example.a321projectprototype.ui.Rewards;

import java.util.Calendar;

public class RewardDisplayBox {
    private String birdName;
    private Calendar timestamp;

    RewardDisplayBox(String birdName, Calendar timestamp) {
        this.birdName = birdName;
        this.timestamp = timestamp;
    }

    public String getBirdName() {
        return birdName;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }
}
