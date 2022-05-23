package com.example.a321projectprototype.ui.Flock;

public class FlockMembersModel<_>
{
    private String groupName;
    private String username;
    private int score;

    FlockMembersModel(String groupName, String username, int score)
    {
        this.groupName = groupName;
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
