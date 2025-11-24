package com.cookandroid.caffeservice.LoginData;

// ⭐️ 필수 임포트 ⭐️
import com.google.gson.annotations.SerializedName;

public class SignupRequest {

    // 1. userId 필드를 JSON으로 변환할 때 'id'로 변경
    @SerializedName("id") // ⬅️ 서버가 기대하는 이름
    private String userId;

    // 2. userPw 필드를 JSON으로 변환할 때 'password'로 변경
    @SerializedName("password") // ⬅️ 서버가 기대하는 이름
    private String userPw;

    // 3. userName 필드를 JSON으로 변환할 때 'name'으로 변경 (서버 코드의 name:realName 부분)
    @SerializedName("name") // ⬅️ 서버가 기대하는 이름 (실명)
    private String userName;

    public SignupRequest(String userId, String userPw, String userName) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
    }

    // (Getter/Setter 코드는 그대로 유지)
    public String getUserId() {
        return userId;
    }
    public String getUserPw() {
        return userPw;
    }
    public String getUserName() {
        return userName;
    }
}