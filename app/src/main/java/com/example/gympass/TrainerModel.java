package com.example.gympass;

public class TrainerModel {

    private int id;
    private int userType;
    private String first_name;
    private String last_name;
    private String birth_date;
    private String email;
    private String password;
    private String telephone;
    String picture;
    private int gym_id;

    public TrainerModel(int id, int userType,String first_name, String last_name, String birth_date, String email, String password, String telephone, String picture, int gym_id) {
        this.id = id;
        this.userType = userType;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.picture = picture;
        this.gym_id = gym_id;
    }

    public TrainerModel() {

    }

    @Override
    public String toString() {
        return id + " " + first_name + " " + last_name + '\n' + email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getGym_id(){
        return gym_id;
    }

    public void setGym_id(int gym_id){
        this.gym_id = gym_id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
