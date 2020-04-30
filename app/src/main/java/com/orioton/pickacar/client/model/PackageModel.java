package com.orioton.pickacar.client.model;

public class PackageModel {

    private String packageName;
    private Double packagePrice;
    private Double packagePricePerKm;
    private Double kilometers;
    private String adminId;

    public PackageModel() {
    }

    public PackageModel(String packageName, Double packagePrice, Double packagePricePerKm, Double kilometers, String adminId) {
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packagePricePerKm = packagePricePerKm;
        this.kilometers = kilometers;
        this.adminId = adminId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Double getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(Double packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Double getPackagePricePerKm() {
        return packagePricePerKm;
    }

    public void setPackagePricePerKm(Double packagePricePerKm) {
        this.packagePricePerKm = packagePricePerKm;
    }

    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
