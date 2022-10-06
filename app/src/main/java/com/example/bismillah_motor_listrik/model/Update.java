package com.example.bismillah_motor_listrik.model;

public class Update {
    private String credit, motor_id, status_motor;

    public Update(String credit, String motor_id, String status_motor) {
        this.credit = credit;
        this.motor_id = motor_id;
        this.status_motor = status_motor;
    }

    public String getMotor_id() {
        return motor_id;
    }

    public String getStatus_motor() {
        return status_motor;
    }

    public String getCredit() {
        return credit;
    }
}
