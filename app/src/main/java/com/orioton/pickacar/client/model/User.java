package com.orioton.pickacar.client.model;

public class User {

    private String userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String userProfilePhotoUrl;
    private String joinedDate;

    public User() {

    }

    public User(String userId, String userName, String userEmail, String userPhone, String userProfilePhotoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userProfilePhotoUrl = userProfilePhotoUrl;
    }


    public User(String userId, String userName, String userEmail, String userPhone, String userProfilePhotoUrl, String joinedDate) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userProfilePhotoUrl = userProfilePhotoUrl;
        this.joinedDate = joinedDate;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserProfilePhotoUrl() {
        return userProfilePhotoUrl;
    }

    public void setUserProfilePhotoUrl(String userProfilePhotoUrl) {
        this.userProfilePhotoUrl = userProfilePhotoUrl;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }
}
