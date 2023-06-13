package com.example.gympass;

public class UserTypeModel {

    private int id;
    private String userType;

    //constructor
    public UserTypeModel(int id, String userType) {
        this.id = id;
        this.userType = userType;
    }

    //toString


    @Override
    public String toString() {
        return id + " " + userType;
    }

    //getters and setters
    public UserTypeModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
