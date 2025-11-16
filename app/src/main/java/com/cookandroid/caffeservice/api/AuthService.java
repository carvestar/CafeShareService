package com.cookandroid.caffeservice.api; // ⚠️ 실제 프로젝트의 패키지 이름으로 변경하세요.

import com.cookandroid.caffeservice.LoginData.LoginRequest; // 2단계에서 만든 Request 모델 import
import com.cookandroid.caffeservice.LoginData.LoginResponse; // 2단계에서 만든 Response 모델 import
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    // 기존 로그인 엔드포인트
    @POST("api/v1/auth/login")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );

    // ⭐️ 새로 추가할 회원가입 엔드포인트 ⭐️
    @POST("api/v1/auth/signup") // 서버의 실제 회원가입 경로로 변경하세요.
    Call<SignupResponse> signup(
            @Body SignupRequest request
    );
}