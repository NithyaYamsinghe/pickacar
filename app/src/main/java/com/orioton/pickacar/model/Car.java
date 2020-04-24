package com.orioton.pickacar.model;

public class Car {
    private String brand;
    private String model;
    private String color;
    private Number releasedYear;
    private Number passengers;
    private String description;

    public Car() {

    }

    public Car(String brand, String model, String color, Number releasedYear, Number passengers, String description) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.releasedYear = releasedYear;
        this.passengers = passengers;
        this.description = description;
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

    public Number getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(Number releasedYear) {
        this.releasedYear = releasedYear;
    }

    public Number getPassengers() {
        return passengers;
    }

    public void setPassengers(Number passengers) {
        this.passengers = passengers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
