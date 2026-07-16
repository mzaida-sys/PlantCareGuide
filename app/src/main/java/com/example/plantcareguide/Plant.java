package com.example.plantcareguide;

import java.io.Serializable;

public class Plant implements Serializable {
    private int id;
    private String name;
    private String category;
    private String waterSchedule;
    private String sunlight;
    private String notes;
    private int userId;


    public Plant(int id, String name, String category, String waterSchedule, String sunlight, String notes, int userId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.waterSchedule = waterSchedule;
        this.sunlight = sunlight;
        this.notes = notes;
        this.userId = userId;
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getWaterSchedule() { return waterSchedule; }
    public String getSunlight() { return sunlight; }
    public String getNotes() { return notes; }
    public int getUserId() { return userId; }
}