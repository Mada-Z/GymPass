package com.example.gympass;

public class GymModel {

    private int id;
    private int userType;
    private String name;
    private String email;
    private String password;

    public GymModel(int id, int userType, String name, String email, String password) {
        this.id = id;
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public GymModel() {

    }

    @Override
    public String toString() {
       return id + " " + name + '\n' + email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
