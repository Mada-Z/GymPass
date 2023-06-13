package com.example.gympass;

public class GymDetailsModel {
    private int id;
    private int id_gym;
    private String name;
    private String open_hours;
    private String close_hours;
    private String street;
    private int adr_nr;
    private String city;
    private String county;
    private double latitude;
    private double longitude;
    private String image;

    public GymDetailsModel(int id, int id_gym, String name, String open_hours, String close_hours, String street, int adr_nr, String city, String county, double latitude, double longitude, String image) {
        this.id = id;
        this.id_gym = id_gym;
        this.name = name;
        this.open_hours = open_hours;
        this.close_hours = close_hours;
        this.street = street;
        this.adr_nr = adr_nr;
        this.city = city;
        this.county = county;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public GymDetailsModel() {

    }

    @Override
    public String toString() {
        return id_gym + " " + name + "\n" + street + ", " + adr_nr +
                ", " + city +
                ", " + county;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_gym() {
        return id_gym;
    }

    public void setId_gym(int id_gym) {
        this.id_gym = id_gym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen_hours() {
        return open_hours;
    }

    public void setOpen_hours(String open_hours) {
        this.open_hours = open_hours;
    }

    public String getClose_hours() {
        return close_hours;
    }

    public void setClose_hours(String close_hours) {
        this.close_hours = close_hours;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getAdr_nr() {
        return adr_nr;
    }

    public void setAdr_nr(int adr_nr) {
        this.adr_nr = adr_nr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
