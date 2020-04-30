package com.orioton.pickacar.driver.model;

public class ReviewModel {

    private String username;
    private String driverName;
    private String title;
    private String review;

    public ReviewModel() {
    }

    public ReviewModel(String username, String driverName, String title, String review) {
        this.username = username;
        this.driverName = driverName;
        this.title = title;
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getTitle() {
        return title;
    }

    public String getReview() {
        return review;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
