package com.orioton.pickacar.admin.model;

public class CarModel {

    // variables

    private String brand;
    private String model;
    private String color;
    private String releasedYear;
    private String passengers;
    private String description;
    private String image;
    private String condition;



    // constructor
    public CarModel() {

    }

    public CarModel(String brand, String model, String color, String releasedYear, String passengers, String description, String image, String condition) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.releasedYear = releasedYear;
        this.passengers = passengers;
        this.description = description;
        this.image = image;
        this.condition = condition;
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

    public String getReleasedYear() {
        return releasedYear;
    }

    public void setReleasedYear(String releasedYear) {
        this.releasedYear = releasedYear;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
