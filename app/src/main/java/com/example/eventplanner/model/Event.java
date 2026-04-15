package com.example.eventplanner.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String category;
    private String location;
    private long dateTimeMillis;

    public Event(String title, String category, String location, long dateTimeMillis) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTimeMillis = dateTimeMillis;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public long getDateTimeMillis() { return dateTimeMillis; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setDateTimeMillis(long millis) { this.dateTimeMillis = millis; }
}
