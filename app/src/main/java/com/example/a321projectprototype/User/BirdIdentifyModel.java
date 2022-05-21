package com.example.a321projectprototype.User;

import com.google.firebase.Timestamp;

public class BirdIdentifyModel
{
    private String bird_name;
    private Timestamp date;
    private String recorded_by;

    public BirdIdentifyModel(){}

    public BirdIdentifyModel(String bird_name, Timestamp date, String recorded_by) {
        this.bird_name = bird_name;
        this.date = date;
        this.recorded_by = recorded_by;
    }

    public String getBird_name() {
        return bird_name;
    }

    public void setBird_name(String bird_name) {
        this.bird_name = bird_name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(String recorded_by) {
        this.recorded_by = recorded_by;
    }
}
