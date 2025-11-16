package com.cookandroid.caffeservice.LoginData; // ⚠️ 반드시 실제 프로젝트의 패키지 이름으로 변경하세요.

/**
 * 로그인 시 서버로 전송할 요청 데이터를 담는 모델
 * 필드 이름(userId, userPw)은 서버 API의 JSON 키와 정확히 일치해야 합니다.
 */
public class LoginRequest {
    private String userId;
    private String userPw;

    // 생성자 (Constructor): 객체 생성 시 ID와 PW를 초기화합니다.
    public LoginRequest(String userId, String userPw) {
        this.userId = userId;
        this.userPw = userPw;
    }

    // Getter: Retrofit(Gson)이 이 메서드를 사용하여 필드 값을 JSON으로 변환합니다.
    public String getUserId() {
        return userId;
    }

    public String getUserPw() {
        return userPw;
    }

    // Setter (선택 사항이지만 안전을 위해 포함)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }
}