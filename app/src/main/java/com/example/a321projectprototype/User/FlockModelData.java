package com.example.a321projectprototype.User;

public class FlockModelData
{
    private String name;
    private int groupNumber;
    private String description;

    public FlockModelData(String name, int groupNumber, String description)
    { this.name = name; this.groupNumber = groupNumber; this.description = description; }

    public FlockModelData(String name, int groupNumber)
    {
        this.name = name;
        this.groupNumber = groupNumber;
    }

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
}
