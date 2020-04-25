package com.orioton.pickacar.model;

public class CarUploadInfo {
    // variables

    private String brand;
    private String color;
    private String condition;
    private String description;
    private String image;
    private String model;
    private String passengers;
    private String releasedYear;
    private String search;

    public CarUploadInfo() {
    }

    public CarUploadInfo(String brand, String color, String condition, String description, String image, String model, String passengers, String releasedYear, String search) {
        this.brand = brand;
        this.color = color;
        this.condition = condition;
        this.description = description;
        this.image = image;
        this.model = model;
        this.passengers = passengers;
        this.releasedYear = releasedYear;
        this.search = search;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
