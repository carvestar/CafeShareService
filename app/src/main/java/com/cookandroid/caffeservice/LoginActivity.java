package com.cookandroid.caffeservice; // ⚠️ 실제 프로젝트의 메인 패키지

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Retrofit 관련 Import
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 우리가 만든 API 및 데이터 모델 Import
import com.cookandroid.caffeservice.api.RetrofitClient;
import com.cookandroid.caffeservice.LoginData.LoginRequest; // ⭐️ 수정된 패키지 경로 적용
import com.cookandroid.caffeservice.LoginData.LoginResponse; // ⭐️ 수정된 패키지 경로 적용


// 이 액티비티는 activity_login.xml 레이아웃을 사용합니다.
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XML 레이아웃 파일(activity_login.xml)을 화면에 연결
        setContentView(R.layout.activity_login);

        // 1. UI 요소 초기화 및 연결
        idEditText = findViewById(R.id.edit_text_id);        // ⚠️ XML ID 확인 필요
        passwordEditText = findViewById(R.id.edit_text_password); // ⚠️ XML ID 확인 필요
        loginButton = findViewById(R.id.button_login);        // ⚠️ XML ID 확인 필요
        signUpText = findViewById(R.id.text_signup);

        // 2. 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 3. 회원가입 텍스트 클릭 리스너 설정
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "회원가입 페이지로 이동", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 사용자의 입력 값을 검증하고 백엔드 로그인 API를 호출하는 메서드입니다.
     */
    private void attemptLogin() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 1. 기본적인 입력 유효성 검사
        if (id.isEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Login Attempt with ID: " + id);

        // 2. 로그인 요청 객체 생성
        LoginRequest requestBody = new LoginRequest(id, password);

        // 3. ⭐️ Retrofit API 호출 시작 및 비동기 처리 ⭐️
        RetrofitClient.getAuthService().login(requestBody).enqueue(new Callback<LoginResponse>() {

            // 서버 응답 도착 (HTTP 통신 성공)
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // HTTP Status Code 200~300 (성공)
                    LoginResponse loginResponse = response.body();

                    if (loginResponse != null && loginResponse.isSuccess()) {
                        // ⭐️ 로그인 성공 로직:
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // 토큰 저장 (추가 구현 필요)
                        String token = loginResponse.getAccessToken();
                        if (token != null) {
                            // TODO: SharedPreferences 등에 토큰 저장 로직 구현
                            // saveAuthToken(token);
                        }

                        // 예시: 메인 화면으로 이동 (필요하다면 주석을 풀고 구현)
                        // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        // startActivity(intent);
                        // finish();

                    } else {
                        // 로그인 실패 (서버 응답에서 success=false인 경우)
                        String errorMessage = (loginResponse != null && loginResponse.getMessage() != null) ?
                                loginResponse.getMessage() : "로그인에 실패했습니다. (서버 응답 오류)";
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // HTTP Status Code 오류 (예: 401 Unauthorized, 404 Not Found, 500 등)
                    Toast.makeText(LoginActivity.this, "로그인 실패: 서버 응답 오류 (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            // 네트워크 통신 자체 실패 (URL 오류, 인터넷 연결 끊김 등)
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN_API", "API Call Failed: " + t.getMessage(), t);
            }
        });
    }
}