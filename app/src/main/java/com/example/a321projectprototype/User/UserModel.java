package com.example.a321projectprototype.User;

public class UserModel
{
    private int id;
    private String name;
    private String username;
    private String password;
    private String email;
    private FlockModelData userFlock;

    public UserModel(int id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        userFlock = null;
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

    public FlockModelData getUserFlock() { return userFlock; }

    public void setUserFlock(FlockModelData userFlock) { this.userFlock = userFlock; }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}
