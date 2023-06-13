package com.example.gympass;

import androidx.annotation.NonNull;

public class AdminModel {

    private int id;
    private int userType;
    private String email;
    private String password;

    public AdminModel(int id, int userType, String email, String password) {
        this.id = id;
        this.userType = userType;
        this.email = email;
        this.password = password;
    }

    public AdminModel() {

    }

    @NonNull
    @Override
    public String toString() {
        return id + " " + email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
