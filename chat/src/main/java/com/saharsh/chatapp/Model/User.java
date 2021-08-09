package com.saharsh.chatapp.Model;

public class User {

    private String image;
    private String phone;
    private String type;
    private String uid;
    private String username;
    private String about;

    public User() {

    }

    public User(String about, String image, String phone, String type, String uid, String username) {
        this.image = image;
        this.phone = phone;
        this.type = type;
        this.uid = uid;
        this.username = username;
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

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }}
