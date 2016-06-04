package com.usimedia.chitchat.model;

/**
 * Created by USI IT on 5/31/2016.
 */
public class LoginModel {

    private String email;
    private String password;

    public LoginModel(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
