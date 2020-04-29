package com.orioton.pickacar.driver.model;

public class JourneyModel {

    private String userId;
    private String username;
    private String phone;
    private String date;
    private String location;
    private String destination;
    private String passengers;
    private String status;
    private String price;

    public JourneyModel() {

    }

    public JourneyModel(String userId, String username, String phone, String date, String location, String destination, String passengers, String status, String price) {
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.date = date;
        this.location = location;
        this.destination = destination;
        this.passengers = passengers;
        this.status = status;
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDestination() {
        return destination;
    }

    public String getPassengers() {
        return passengers;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
