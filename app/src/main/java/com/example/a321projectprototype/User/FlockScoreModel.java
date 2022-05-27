package com.example.a321projectprototype.User;

public class FlockScoreModel {

    private String flockname;
    private int scorethisweek;
    private int scorethismonth;
    private int scorethisyear;
    private int totalScore;
    private String created_at;
    private String updated_at;

    public FlockScoreModel() {
    }

    public FlockScoreModel(String flockname, int scorethisweek, int scorethismonth, int scorethisyear, int totalScore, String created_at, String updated_at) {
        this.flockname = flockname;
        this.scorethisweek = scorethisweek;
        this.scorethismonth = scorethismonth;
        this.scorethisyear = scorethisyear;
        this.totalScore = totalScore;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getScorethisweek() {
        return scorethisweek;
    }

    public void setScorethisweek(int scorethisweek) {
        this.scorethisweek = scorethisweek;
    }

    public int getScorethismonth() {
        return scorethismonth;
    }

    public void setScorethismonth(int scorethismonth) {
        this.scorethismonth = scorethismonth;
    }

    public int getScorethisyear() {
        return scorethisyear;
    }

    public void setScorethisyear(int scorethisyear) {
        this.scorethisyear = scorethisyear;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getFlockname() {
        return flockname;
    }

    public void setFlockname(String flockname) {
        this.flockname = flockname;
    }
}