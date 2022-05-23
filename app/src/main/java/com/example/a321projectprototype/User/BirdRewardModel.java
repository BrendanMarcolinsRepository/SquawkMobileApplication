package com.example.a321projectprototype.User;

public class BirdRewardModel
{
    private String bird_name;
    private String bird_status;
    private String bird_url;
    private String bird_image;

    public BirdRewardModel(){}

    public BirdRewardModel(String bird_name, String bird_status, String bird_url, String bird_image) {
        this.bird_name = bird_name;
        this.bird_status = bird_status;
        this.bird_url = bird_url;
        this.bird_image = bird_image;
    }

    public String getBird_image() {
        return bird_image;
    }

    public void setBird_image(String bird_image) {
        this.bird_image = bird_image;
    }

    public String getBird_url() {
        return bird_url;
    }

    public void setBird_url(String bird_url) {
        this.bird_url = bird_url;
    }

    public String getBird_name() {
        return bird_name;
    }

    public void setBird_name(String bird_name) {
        this.bird_name = bird_name;
    }

    public String getBird_status() {
        return bird_status;
    }

    public void setBird_status(String bird_status) {
        this.bird_status = bird_status;
    }
}
