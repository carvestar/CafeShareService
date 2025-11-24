package com.cookandroid.caffeservice.api;

import com.cookandroid.caffeservice.LoginData.SignupRequest;
import com.cookandroid.caffeservice.LoginData.SignupResponse;
import com.cookandroid.caffeservice.LoginData.LoginRequest;
import com.cookandroid.caffeservice.LoginData.LoginResponse;
// ⭐️ Google 로그인 요청 모델 Import (추가 필요) ⭐️
import com.cookandroid.caffeservice.LoginData.GoogleLoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AuthService {

    // 1. 일반 로그인 엔드포인트 (상대 경로로 수정)
    // BASE_URL (예: http://10.0.2.2:3000/) + "api/v1/auth/login"
    @POST("/api/v1/users/login") // ⚠️ 서버의 실제 경로에 맞게 수정하세요.
    Call<LoginResponse> login(
            @Body LoginRequest request
    );

    // 2. 회원가입 엔드포인트 (상대 경로로 수정)
    // BASE_URL + "api/v1/auth/signup"
    @POST("/api/v1/users/register") // ⚠️ 서버의 실제 경로에 맞게 수정하세요.
    Call<SignupResponse> signup(
            @Body SignupRequest request
    );

    // ⭐️ 3. Google 로그인 엔드포인트 추가 ⭐️
    // BASE_URL + "api/v1/auth/google"
    @POST("/api/v1/auth/google") // Node.js 서버에서 설정한 Google 로그인 라우터 경로
    Call<LoginResponse> googleLogin(
            @Body GoogleLoginRequest request
    );
}