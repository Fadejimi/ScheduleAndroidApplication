package com.model;

import java.util.List;

/**
 * Created by Fadejimi on 7/3/18.
 */

public class Schedule {

    private String name;
    private String description;
    private double percentage;
    private double rating;
    private List<Task> tasks;

    public Schedule(String name, String description, double percentage) {
        setName(name);
        setDescription(description);
        setPercentage(percentage);
    }

    public Schedule(String name, String description, double percentage, List<Task> tasks) {
        setName(name);
        setDescription(description);
        setPercentage(percentage);
        setTasks(tasks);
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
        double rate = percentage/100 * 5;
        setRating(rate);
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Task> getTasks() {
        return this.tasks;
    }
}
