package com.cookandroid.caffeservice.LoginData;

// ⭐️ 필수 임포트: JSON 변환 시 필드 이름 변경을 위해 필요합니다. ⭐️
import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    // 1. ⭐️ 필드 이름을 'id'로 변경 ⭐️
    @SerializedName("name") // ⬅️ 서버 DB 컬럼명이 'name'이므로 JSON 키는 'name'으로 전송
    private String id;

    // 2. 비밀번호 필드는 그대로 'userPw' 유지 (JSON 키는 'password')
    @SerializedName("password")
    private String userPw;

    // 생성자 (Constructor): 객체 생성 시 ID와 PW를 초기화합니다.
    public LoginRequest(String id, String userPw) {
        this.id = id;
        this.userPw = userPw;
    }

    // Getter (Retrofit이 JSON을 만들기 위해 필요함)
    public String getId() {
        return id;
    }
    public String getUserPw() {
        return userPw;
    }
}