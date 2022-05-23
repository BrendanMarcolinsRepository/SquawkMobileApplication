package com.example.a321projectprototype.User;

public class UserScore  implements Comparable<UserScore>{
    private String userId;
    private int scoreThisWeek;
    private int scoreThisMonth;
    private int scoreThisYear;
    private int totalScore;
    private String created_at;
    private String updated_at;
    private String username;

    public UserScore() {
    }

    public UserScore(String userId, int scoreThisWeek, int scoreThisMonth, int scoreThisYear, int totalScore, String created_at, String updated_at, String username) {
        this.userId = userId;
        this.scoreThisWeek = scoreThisWeek;
        this.scoreThisMonth = scoreThisMonth;
        this.scoreThisYear = scoreThisYear;
        this.totalScore = totalScore;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int compareTo(UserScore o) {
        if(this.getTotalScore() > o.getTotalScore()){
            return 1;
        }else if(this.getTotalScore() < o.getTotalScore()){
            return -1;
        }else{
            return 0;
        }
    }
}
