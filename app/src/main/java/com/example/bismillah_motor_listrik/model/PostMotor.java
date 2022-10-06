package com.example.bismillah_motor_listrik.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostMotor {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("range")
    @Expose
    private Integer range;
    @SerializedName("data")
    @Expose
    private dataMotor data;
    @SerializedName("battery")
    @Expose
    private Integer battery;
    @SerializedName("off")
    @Expose
    private Integer off;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public dataMotor getData() {
        return data;
    }

    public void setData(dataMotor data) {
        this.data = data;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getOff() {
        return off;
    }

    public void setOff(Integer off) {
        this.off = off;
    }
}
