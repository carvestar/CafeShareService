package com.cookandroid.caffeservice.api; // ⚠️ 실제 프로젝트의 패키지 이름으로 변경하세요.

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ⭐️ 서버의 기본 URL을 여기에 입력하세요. (예: "http://192.168.0.10:8080/")
    private static final String BASE_URL = "https://your-server-domain.com/";

    private static Retrofit retrofit = null;
    private static AuthService authService = null;

    // 1. Retrofit 인스턴스를 초기화하고 반환하는 메서드
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Gson 컨버터 사용
                    .build();
        }
        return retrofit;
    }

    // 2. AuthService 인터페이스를 구현하는 실제 서비스 객체를 반환하는 메서드
    public static AuthService getAuthService() {
        if (authService == null) {
            // Retrofit 인스턴스를 통해 AuthService 객체를 생성
            authService = getRetrofitInstance().create(AuthService.class);
        }
        return authService;
    }
}