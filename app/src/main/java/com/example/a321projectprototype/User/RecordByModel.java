package com.example.a321projectprototype.User;

public class RecordByModel {
    private String bird_name;
    private String recorded_by;

    public RecordByModel() {
    }

    public RecordByModel(String bird_name, String recorded_by) {
        this.bird_name = bird_name;
        this.recorded_by = recorded_by;
    }

    public String getBird_name() {
        return bird_name;
    }

    public void setBird_name(String bird_name) {
        this.bird_name = bird_name;
    }

    public String getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(String recorded_by) {
        this.recorded_by = recorded_by;
    }
}
