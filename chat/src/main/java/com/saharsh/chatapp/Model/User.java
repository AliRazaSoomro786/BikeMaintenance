package com.saharsh.chatapp.Model;

public class User {

    private String image;
    private String phone;
    private String userType;
    private String uid;
    private String name;
    private String about;

    public User() {

    }

    public User(String about, String image, String phone, String userType, String uid, String name) {
        this.image = image;
        this.phone = phone;
        this.userType = userType;
        this.uid = uid;
        this.name = name;
        this.about = about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return userType;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }}
