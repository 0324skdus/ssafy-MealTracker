package com.ssafy.yamyam.dto;

public class LoginRequestDTO {
    private String userId;
    private String userPassword;

    public LoginRequestDTO() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
