package com.example.a321projectprototype.ui.Flock;

public class FlockModelData
{
    private String name;
    private int groupNumber;


    FlockModelData(String name, int groupNumber)
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
}
