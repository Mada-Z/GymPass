package com.example.gympass;

public class SubscriptionModel {
    private int id;
    private String subscriptionType;
    private String description;
    private int price;
    private int available;

    public SubscriptionModel(int id, String subscriptionType, String description, int price, int available) {
        this.id = id;
        this.subscriptionType = subscriptionType;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public SubscriptionModel() {

    }

    @Override
    public String toString() {
        return subscriptionType + ": " + description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
