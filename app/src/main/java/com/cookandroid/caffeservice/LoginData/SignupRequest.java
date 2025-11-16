package com.cookandroid.caffeservice.LoginData;

public class SignupRequest {
    private String userId;
    private String userPw;
    private String userName; // 예시: 추가 필드

    public SignupRequest(String userId, String userPw, String userName) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
    }

    // Getter (필수)
    public String getUserId() {
        return userId;
    }
    public String getUserPw() {
        return userPw;
    }
    public String getUserName() {
        return userName;
    }
    // Setter는 생략 가능
}