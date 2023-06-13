package com.example.gympass;

import androidx.annotation.NonNull;

public class ClassesModel {
    private int id;
    private int gymId;
    private int trainerId;
    private String name;
    private String description;
    private String date;
    private String hour;
    private String trainerName;

    public ClassesModel(int id, int gymId, int trainerId, String name, String description, String date, String hour, String trainerName) {
        this.id = id;
        this.gymId = gymId;
        this.trainerId = trainerId;
        this.name = name;
        this.description = description;
        this.date = date;
        this.hour = hour;
        this.trainerName = trainerName;
    }

    public ClassesModel() {
    }

    @NonNull
    @Override
    public String toString() {
        return name + " " + description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
