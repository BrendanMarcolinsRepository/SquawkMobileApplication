package com.example.a321projectprototype.User;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Date;

public class FlockModelData implements Serializable {
    private String flockId;
    private String userId;
    private String name;
    private int memberCount;
    private String description;
    private String created_at;
    private String updated_at;

    public FlockModelData() {
    }

    public FlockModelData(String flockId, String userId, String name, int memberCount, String description, String created_at, String updated_at) {
        this.flockId = flockId;
        this.userId = userId;
        this.name = name;
        this.memberCount = memberCount;
        this.description = description;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getFlockId() {
        return flockId;
    }

    public void setFlockId(String flockId) {
        this.flockId = flockId;
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

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}


