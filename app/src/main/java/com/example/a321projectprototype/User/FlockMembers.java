package com.example.a321projectprototype.User;

public class FlockMembers {
    private String usersId;
    private String flockNameId;
    private String created_at;


    public FlockMembers() {
    }

    public FlockMembers(String usersId, String flockNameId, String created_at) {

        this.usersId = usersId;
        this.flockNameId = flockNameId;
        this.created_at = created_at;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getFlockNameId() {
        return flockNameId;
    }

    public void setFlockNameId(String flockNameId) {
        this.flockNameId = flockNameId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}