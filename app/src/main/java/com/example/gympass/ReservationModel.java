package com.example.gympass;

public class ReservationModel {
    private int userId;
    private int classId;

    public ReservationModel(int userId, int classId) {
        this.userId = userId;
        this.classId = classId;
    }

    public ReservationModel() {
    }

    @Override
    public String toString() {
       return classId + "";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
