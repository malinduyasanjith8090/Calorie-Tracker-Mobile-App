package com.example.calorieapp;

public class Meal {
    private String id;
    private String name;
    private String type;
    private String calories;
    private String photoUrl;

    public Meal() {
        // Default constructor required for calls to DataSnapshot.getValue(Meal.class)
    }

    public Meal(String name, String type, String calories, String photoUrl) {
        this.id = id;  // Ensure the id is passed when creating a Meal object
        this.name = name;
        this.type = type;
        this.calories = calories;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
