package com.cookandroid.caffeservice.LoginData;

/**
 * 회원가입 성공/실패 시 서버로부터 받을 응답 데이터를 담는 모델
 */
public class SignupResponse {

    // ⭐️ 서버가 보내는 필드 이름과 정확히 일치해야 합니다. ⭐️
    private boolean success;
    private String message;
    private String accessToken; // (선택 사항: 서버가 토큰을 보낼 경우)

    // Getter: Activity에서 isSuccess()를 호출할 때 사용됩니다.
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    // Setter는 생략합니다.
}