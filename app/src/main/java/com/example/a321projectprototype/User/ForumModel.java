package com.example.a321projectprototype.User;

public class ForumModel
{
    private int id;
    private String username;
    private String topic;
    private String description;
    private boolean privatePost;

    public ForumModel()
    {

    }

    public ForumModel(int id, String username, String topic, String description, boolean privatePost)
    {
        this.id = id;
        this.username = username;
        this.topic = topic;
        this.description = description;
        this.privatePost = privatePost;
    }

    public ForumModel(int id, String username, String topic, String description)
    {
        this.id = id;
        this.username = username;
        this.topic = topic;
        this.description = description;

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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPrivatePost() {
        return privatePost;
    }

    public void setPrivatePost(boolean privatePost) {
        this.privatePost = privatePost;
    }
}
