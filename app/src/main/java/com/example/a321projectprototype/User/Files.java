package com.example.a321projectprototype.User;

import com.google.firebase.Timestamp;

public class Files
{
    private String created_at;
    private String description;
    private String filename;
    private String path;
    private String updated_at;
    private String uploadedBy;

    public Files(String created_at, String description, String filename, String path, String updated_at, String uploadedBy) {
        this.created_at = created_at;
        this.description = description;
        this.filename = filename;
        this.path = path;
        this.updated_at = updated_at;
        this.uploadedBy = uploadedBy;
    }

    public Files() {}

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}


