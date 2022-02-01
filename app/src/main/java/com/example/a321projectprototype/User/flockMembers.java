package com.example.a321projectprototype.User;

public class flockMembers
{
    private String users;
    private String flockName;


    public flockMembers() {
    }

    public flockMembers(String users, String flockName) {
        this.users = users;
        this.flockName = flockName;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getFlockName() {
        return flockName;
    }

    public void setFlockName(String flockName) {
        this.flockName = flockName;
    }
}
