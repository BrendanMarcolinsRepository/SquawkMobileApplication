package com.example.a321projectprototype.User;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private int id;
    private String name;
    private String username;
    private String email;

    public UserModel() { }


    public UserModel(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
