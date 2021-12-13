package com.example.a321projectprototype.User;

public class FlockModelData
{
    private int id;
    private String name;
    private int groupNumber;
    private String description;
    private boolean privateFlock = true;

    public FlockModelData(){}

    public FlockModelData(int id, String name, int groupNumber, String description, boolean privateFlock) { this.id = id;this.name = name; this.groupNumber = groupNumber; this.description = description; this.privateFlock = privateFlock; }

    public FlockModelData(int id, String name, int groupNumber) { this.id = id;this.name = name;this.groupNumber = groupNumber; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public boolean isPrivateFlock() {
        return privateFlock;
    }

    public void setPrivateFlock(boolean privateFlock) {
        this.privateFlock = privateFlock;
    }
}
