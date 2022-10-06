package com.example.bismillah_motor_listrik.model;

public class Motor {
    private String battery, jarak, latitude, longitude, user_id;

    public Motor(String battery, String jarak, String latitude, String longitude, String user_id) {
        this.battery = battery;
        this.jarak = jarak;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user_id = user_id;
    }

    public String getBattery() {
        return battery;
    }

    public String getJarak() {
        return jarak;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUser_id() {
        return user_id;
    }
}
