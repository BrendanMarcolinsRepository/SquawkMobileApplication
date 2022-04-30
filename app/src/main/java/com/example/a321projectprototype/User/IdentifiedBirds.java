package com.example.a321projectprototype.User;

public class IdentifiedBirds {
    private String bird_name;
    private String data;
    private String recorded_by;

    public IdentifiedBirds() {

    }

    public String getBird_name() {
        return bird_name;
    }

    public void setBird_name(String bird_name) {
        this.bird_name = bird_name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(String recorded_by) {
        this.recorded_by = recorded_by;
    }
}
