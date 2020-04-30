package com.orioton.pickacar.client.model;

import android.support.v4.app.INotificationSideChannel;

public class Subscription {

    private String userId;
    private String packageName;
    private Integer kilometers;
    private Integer pricePerKilo;
    private Integer packagePrice;

    public Subscription() {
    }

    public Subscription(String userId, String packageName, Integer kilometers, Integer pricePerKilo, Integer packagePrice) {
        this.userId = userId;
        this.packageName = packageName;
        this.kilometers = kilometers;
        this.pricePerKilo = pricePerKilo;
        this.packagePrice = packagePrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public Integer getPricePerKilo() {
        return pricePerKilo;
    }

    public void setPricePerKilo(Integer pricePerKilo) {
        this.pricePerKilo = pricePerKilo;
    }

    public Integer getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Integer packagePrice) {
        this.packagePrice = packagePrice;
    }
}
