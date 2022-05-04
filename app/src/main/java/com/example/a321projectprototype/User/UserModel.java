package com.example.a321projectprototype.User;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private String userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private String photo_url;

    public UserModel() { }


    public UserModel(String userId,String name, String username, String email,String password, String photo_url) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.photo_url = photo_url;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
