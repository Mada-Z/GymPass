package com.example.gympass;

public class ReservationModel {
    private int userId;
    private int classId;
    private String className;
    private String date;
    private String hour;
    private String trainerName;

    public ReservationModel(int userId, int classId, String className, String date, String hour, String trainerName) {
        this.userId = userId;
        this.classId = classId;
        this.className = className;
        this.date = date;
        this.hour = hour;
        this.trainerName = trainerName;
    }

    @Override
    public String toString() {
        return className + ' ' + date + ' ' + hour + ' ' + trainerName;
    }

    public ReservationModel() {
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
