package com.ticketapp.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String date;
    private String location;
    private String category;

    public Event() {}

    public Event(String title, String date, String location, String category) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDate(String date) { this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setCategory(String category) { this.category = category; }
}