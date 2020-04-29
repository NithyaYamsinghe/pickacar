package com.orioton.pickacar.admin.model;

public class PackageModel {

    private String packageName;
    private Double packagePrice;
    private Double packagePricePerKm;
    private String adminId;

    public PackageModel() {
    }

    public PackageModel(String packageName, Double packagePrice, Double packagePricePerKm, String adminId) {
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.packagePricePerKm = packagePricePerKm;
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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
