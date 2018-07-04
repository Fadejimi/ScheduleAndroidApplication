package com.model;

/**
 * Created by Fadejimi on 7/4/18.
 */

public class Task {

    private String name;
    private String description;
    private double percentage;
    private double rating;

    public Task(String name, String description, double percentage) {
        setName(name);
        setDescription(description);
        setPercentage(percentage);
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

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;

        double rate = percentage /100 * 5;
        setRating(rate);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return this.rating;
    }
}

