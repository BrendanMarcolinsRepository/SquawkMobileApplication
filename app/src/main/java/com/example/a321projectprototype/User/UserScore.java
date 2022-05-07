package com.example.a321projectprototype.User;

public class UserScore {
    private String userId;
    private int scoreThisWeek;
    private int scoreThisMonth;
    private int scoreThisYear;
    private int totalScore;
    private String created_at;
    private String updated_at;

    public UserScore() {
    }

    public UserScore(String userId, int scoreThisWeek, int scoreThisMonth, int scoreThisYear, int totalScore, String created_at, String updated_at) {
        this.userId = userId;
        this.scoreThisWeek = scoreThisWeek;
        this.scoreThisMonth = scoreThisMonth;
        this.scoreThisYear = scoreThisYear;
        this.totalScore = totalScore;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScoreThisWeek() {
        return scoreThisWeek;
    }

    public void setScoreThisWeek(int scoreThisWeek) {
        this.scoreThisWeek = scoreThisWeek;
    }

    public int getScoreThisMonth() {
        return scoreThisMonth;
    }

    public void setScoreThisMonth(int scoreThisMonth) {
        this.scoreThisMonth = scoreThisMonth;
    }

    public int getScoreThisYear() {
        return scoreThisYear;
    }

    public void setScoreThisYear(int scoreThisYear) {
        this.scoreThisYear = scoreThisYear;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
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
}
