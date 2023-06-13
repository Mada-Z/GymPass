package com.example.gympass;

public class UserSubscriptionModel {
    private int id;
    private int userId;
    private int subscriptionId;
    private String dateStart;
    private String dateEnd;

    public UserSubscriptionModel(int id, int userId, int subscriptionId, String dateStart, String dateEnd) {
        this.id = id;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public UserSubscriptionModel() {
    }

    @Override
    public String toString() {
        return "valabil pana la data: " + dateEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
}
