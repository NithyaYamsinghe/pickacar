package com.orioton.pickacar.model;

public class CarModel {

    // variables

    private String brand;
    private String model;
    private String color;
    private int releasedYear;
    private int passengers;
    private String description;
    private String image;


    // constructor
    public CarModel() {

    }

    public CarModel(String brand, String model, String image) {
        this.brand = brand;
        this.model = model;
        this.image = image;
    }

    public CarModel(String brand, String model, String color, int releasedYear, int passengers, String description, String image) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.releasedYear = releasedYear;
        this.passengers = passengers;
        this.description = description;
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(int releasedYear) {
        this.releasedYear = releasedYear;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
