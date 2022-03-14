package com.example.a321projectprototype.User;

import java.io.Serializable;

public class CommentModel implements Serializable

{
    private String created_at;
    private String content;
    private String postId;
    private String userId;
    private String username;
    private String updated_at;


    public CommentModel(String created_at, String content, String postId, String userId, String username, String updated_at) {
        this.created_at = created_at;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.updated_at = updated_at;
    }

    public CommentModel()
    {

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
