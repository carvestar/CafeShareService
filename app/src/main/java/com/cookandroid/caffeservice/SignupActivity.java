package com.cookandroid.caffeservice; // ⚠️ 메인 패키지

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Retrofit 및 데이터 모델 Import
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.cookandroid.caffeservice.api.RetrofitClient;
import com.cookandroid.caffeservice.LoginData.SignupRequest; // Signup 요청 모델
import com.cookandroid.caffeservice.LoginData.SignupResponse; // Signup 응답 모델


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ⚠️ 회원가입 XML 레이아웃 파일 이름으로 변경하세요.
        setContentView(R.layout.activity_signup);

        // 1. UI 요소 초기화 및 연결
        idEditText = findViewById(R.id.edit_text_signup_id);
        passwordEditText = findViewById(R.id.edit_text_signup_password);
        nameEditText = findViewById(R.id.edit_text_signup_name);
        signupButton = findViewById(R.id.button_signup_confirm);

        // 2. 가입 버튼 클릭 리스너 설정
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
    }

    /**
     * 사용자의 입력 값을 검증하고 백엔드 회원가입 API를 호출하는 메서드입니다.
     */
    private void attemptSignup() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        // 1. 기본적인 입력 유효성 검사
        if (id.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 회원가입 요청 객체 생성
        SignupRequest requestBody = new SignupRequest(id, password, name);

        // 3. ⭐️ Retrofit API 호출 시작 및 비동기 처리 ⭐️
        RetrofitClient.getAuthService().signup(requestBody).enqueue(new Callback<SignupResponse>() {

            // 서버 응답 도착 (HTTP 통신 성공)
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if (response.isSuccessful()) {
                    // HTTP Status Code 200~300 (성공)
                    SignupResponse signupResponse = response.body();

                    if (signupResponse != null && signupResponse.isSuccess()) {
                        // ⭐️ 회원가입 성공 로직:
                        Toast.makeText(SignupActivity.this, signupResponse.getMessage() + " 이제 로그인하세요.", Toast.LENGTH_LONG).show();

                        // 성공 후 로그인 화면으로 이동
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // 현재 화면 닫기

                    } else {
                        // 회원가입 실패 (서버 응답에서 success=false인 경우, 예: ID 중복)
                        String errorMessage = (signupResponse != null && signupResponse.getMessage() != null) ?
                                signupResponse.getMessage() : "회원가입에 실패했습니다. (서버 응답 오류)";
                        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // HTTP Status Code 오류 (예: 400 Bad Request, 500 Internal Server Error 등)
                    Toast.makeText(SignupActivity.this, "회원가입 실패: 서버 응답 오류 (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            // 네트워크 통신 자체 실패 (URL 오류, 인터넷 연결 끊김 등)
            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "네트워크 연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "API Call Failed: " + t.getMessage(), t);
            }
        });
    }
}