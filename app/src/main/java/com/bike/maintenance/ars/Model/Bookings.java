package com.bike.maintenance.ars.Model;

public class Bookings {

    private String address;
    private String company;
    private String email;
    private String mechanicuid;
    private String name;
    private String number;
    private String powerengine;
    private String repairdescription;
    private String timestamp;
    private String key;
    private String uid;
    private boolean status;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMechanicuid() {
        return mechanicuid;
    }

    public void setMechanicuid(String mechanicuid) {
        this.mechanicuid = mechanicuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPowerengine() {
        return powerengine;
    }

    public void setPowerengine(String powerengine) {
        this.powerengine = powerengine;
    }

    public String getRepairdescription() {
        return repairdescription;
    }

    public void setRepairdescription(String repairdescription) {
        this.repairdescription = repairdescription;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
