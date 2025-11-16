package com.cookandroid.caffeservice.LoginData;

public class SignupResponse {
    private boolean success;
    private String message;

    // Getter (필수)
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return message;
    }
    // Setter는 생략 가능
}