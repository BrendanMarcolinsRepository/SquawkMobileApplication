package com.example.a321projectprototype.User;

import com.google.firebase.Timestamp;

import java.util.Date;

public class FlockModelData {
    private int id;
    private String name;
    private int memberAmount;
    private String description;
    private Timestamp created_at;
    private Timestamp updated_at;

    public FlockModelData() {
    }

    public FlockModelData(Timestamp created_at, String description, int memberAmount, String name, Timestamp updated_at,int id) {
        this.name = name;
        this.memberAmount = memberAmount;
        this.description = description;
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(int memberAmount) {
        this.memberAmount = memberAmount;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}


