package com.bike.maintenance.ars.Model;

public class Mechanic {
    private double lat;
    private double lng;
    private String name;
    private String uid;
    private String userType;
    private String phone;

    public Mechanic(double lat, double lng, String name, String uid, String userType, String phone) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.uid = uid;
        this.userType = userType;
        this.phone = phone;
    }

    public Mechanic() {

    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getUserType() {
        return userType;
    }

    public String getPhone() {
        return phone;
    }
}
