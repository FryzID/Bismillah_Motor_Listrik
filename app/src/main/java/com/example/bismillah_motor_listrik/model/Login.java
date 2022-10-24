package com.example.bismillah_motor_listrik.model;

public class Login {

    private String success, message, token, id, saldo;

    public Login(String success, String message, String token, String id, String saldo) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.id = id;
        this.saldo = saldo;
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getSaldo() {
        return saldo;
    }
}