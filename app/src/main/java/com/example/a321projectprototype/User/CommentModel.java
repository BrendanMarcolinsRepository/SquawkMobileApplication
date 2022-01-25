package com.example.a321projectprototype.User;

import java.io.Serializable;

public class CommentModel implements Serializable
{
    private int id;
    private String username;
    private String topic;
    private String comment;
    private boolean privatePost;

    public CommentModel()
    {

    }

    public CommentModel(int id, String username, String topic, String comment, boolean privatePost)
    {
        this.id = id;
        this.username = username;
        this.topic = topic;
        this.comment = comment;
        this.privatePost = privatePost;
    }

    public CommentModel(int id, String username, String topic, String comment)
    {
        this.id = id;
        this.username = username;
        this.topic = topic;
        this.comment = comment;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return comment;
    }

    public void setDescription(String comment) {
        this.comment = comment;
    }

    public boolean getPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost(boolean privatePost) {
        this.privatePost = privatePost;
    }
}
