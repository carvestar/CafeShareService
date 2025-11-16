package com.cookandroid.caffeservice.LoginData; // ⚠️ 실제 패키지 이름으로 변경하세요.

/**
 * 로그인 성공/실패 시 서버로부터 받을 응답 데이터를 담는 모델
 */
public class LoginResponse {
    private boolean success;
    private String message;
    private String accessToken; // 인증 토큰

    // Getter: Gson이 JSON을 객체로 변환 후, Activity에서 접근 시 사용
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    // Setter (선택 사항)
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}