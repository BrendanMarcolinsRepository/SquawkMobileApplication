package com.example.a321projectprototype.User;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String userFlock;

    public UserModel() { }

    public UserModel( String name, String username, String password, String email) {

        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        userFlock = null;
    }

    public UserModel(int id, String name,String username,String email, String password, String userFlock ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userFlock = userFlock;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserFlock() { return userFlock; }

    public void setUserFlock(String userFlock) { this.userFlock = userFlock; }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
