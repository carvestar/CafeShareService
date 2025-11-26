package com.cookandroid.caffeservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 앱 시작 시 일정 시간 동안 표시되는 스플래시 화면 액티비티입니다.
 */
public class SplashActivity extends AppCompatActivity {

    // 스플래시 화면 유지 시간 (5000ms = 5초)
    private static final int SPLASH_TIME_OUT = 5000;

    // TODO: 회원가입 화면 클래스 이름을 SignUpActivity로 가정합니다.
    // 실제 SignUpActivity.java 파일이 없으면 이 이름으로 새로 생성해야 합니다.
    private Class<?> nextActivity = LoginActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 화면을 전체 화면으로 표시 (상태바 숨기기)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // XML 레이아웃 파일 연결 (R.layout.activity_splash는 직접 생성해야 합니다)
        setContentView(R.layout.activity_splash);

        // 지정된 시간이 지난 후 다음 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // SignUpActivity로 이동
                Intent intent = new Intent(SplashActivity.this, nextActivity);
                startActivity(intent);

                // 현재 스플래시 액티비티를 종료 (뒤로가기 버튼으로 돌아오지 않도록)
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}