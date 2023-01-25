package com.example.a321projectprototype.User;

import java.io.Serializable;

public class ForumModel implements Serializable
{

    private String userId;
    private String postId;
    private String username;
    private String title;
    private String description;
    private String created_at;
    private String updated_at;



    public ForumModel(String postId, String created_at, String description, String title, String updated_at, String userId, String username)
    {
        this.postId = postId;
        this.created_at = created_at;
        this.username = username;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.updated_at = updated_at;

    }

    public ForumModel(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
