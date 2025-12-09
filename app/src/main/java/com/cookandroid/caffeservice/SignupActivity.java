//package com.cookandroid.caffeservice;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//// ⭐️ Handler와 Looper Import 추가 ⭐️
//import android.os.Handler;
//import android.os.Looper;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//// Retrofit 및 데이터 모델 Import
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import com.cookandroid.caffeservice.api.RetrofitClient;
//import com.cookandroid.caffeservice.LoginData.SignupRequest;
//import com.cookandroid.caffeservice.LoginData.SignupResponse;
//import com.cookandroid.caffeservice.LoginActivity;
//
//import java.io.IOException;
//
//public class SignupActivity extends AppCompatActivity {
//
//    private static final String TAG = "SignupActivity";
//
//    private EditText idEditText;
//    private EditText passwordEditText;
//    private EditText nameEditText;
//    private Button signupButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        // 1. UI 요소 초기화 및 연결
//        idEditText = findViewById(R.id.edit_text_signup_id);
//        passwordEditText = findViewById(R.id.edit_text_signup_password);
//        nameEditText = findViewById(R.id.edit_text_signup_name);
//        signupButton = findViewById(R.id.button_signup_confirm);
//
//        // 2. 가입 버튼 클릭 리스너 설정
//        signupButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                attemptSignup();
//            }
//        });
//    }
//
//    /**
//     * 사용자의 입력 값을 검증하고 백엔드 회원가입 API를 호출하는 메서드입니다.
//     */
//    private void attemptSignup() {
//        String id = idEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String name = nameEditText.getText().toString().trim();
//
//        // 1. 기본적인 입력 유효성 검사
//        if (id.isEmpty() || password.isEmpty() || name.isEmpty()) {
//            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // 2. 회원가입 요청 객체 생성
//        SignupRequest requestBody = new SignupRequest(id, password, name);
//
//        // 3. ⭐️ Retrofit API 호출 시작 및 비동기 처리 ⭐️
//        RetrofitClient.getAuthService().signup(requestBody).enqueue(new Callback<SignupResponse>() {
//
//            // 서버 응답 도착 (HTTP 통신 성공)
//            @Override
//            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
//                Log.d(TAG, "Server Response Code: " + response.code());
//
//                if (response.isSuccessful()) {
//                    SignupResponse signupResponse = response.body();
//
//                    if (signupResponse != null && signupResponse.isSuccess()) {
//                        // ⭐️ 회원가입 성공 로직:
//                        String successMessage = signupResponse.getMessage() != null ?
//                                signupResponse.getMessage() : "회원가입에 성공했습니다.";
//
//                        Toast.makeText(SignupActivity.this, successMessage + " 이제 로그인하세요.", Toast.LENGTH_LONG).show();
//                        Log.i(TAG, "Signup Success: " + successMessage);
//
//                        // ⭐️⭐️ 최종 수정: Handler를 사용한 지연 실행 적용 (500ms) ⭐️⭐️
//                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//
//                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                            // 화면 스택을 정리하는 플래그 유지
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                            startActivity(intent);
//
//                            // 현재 화면 닫기 (지연 후 실행)
//                            finish();
//                        }, 500); // 0.5초 지연
//
//                    } else {
//                        // 회원가입 실패 (서버 응답에서 success=false인 경우, 예: ID 중복)
//                        String errorMessage = (signupResponse != null && signupResponse.getMessage() != null) ?
//                                signupResponse.getMessage() : "회원가입에 실패했습니다. (서버 응답 형식 오류)";
//
//                        Toast.makeText(SignupActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                        Log.w(TAG, "Signup Failed (Server Logic): " + errorMessage);
//                    }
//                } else {
//                    // HTTP Status Code 오류 (예: 400 Bad Request, 500 Internal Server Error 등)
//                    String errorMsg = "서버 응답 오류 (" + response.code() + ")";
//
//                    if (response.errorBody() != null) {
//                        try {
//                            String errorBody = response.errorBody().string();
//                            errorMsg += ". 상세: " + errorBody;
//                            Log.e(TAG, "Signup Failed (HTTP Error " + response.code() + "): " + errorBody);
//                        } catch (IOException e) {
//                            Log.e(TAG, "Error parsing error body: " + e.getMessage());
//                        }
//                    }
//
//                    Toast.makeText(SignupActivity.this, "회원가입 실패: " + errorMsg, Toast.LENGTH_LONG).show();
//                }
//            }
//
//            // 네트워크 통신 자체 실패 (URL 오류, 인터넷 연결 끊김 등)
//            @Override
//            public void onFailure(Call<SignupResponse> call, Throwable t) {
//                String networkError = "네트워크 연결 실패: " + t.getMessage();
//                Toast.makeText(SignupActivity.this, networkError, Toast.LENGTH_LONG).show();
//                Log.e(TAG, "API Call Failed: " + t.getMessage(), t);
//            }
//        });
//    }
//}


package com.cookandroid.caffeservice;

import android.content.Intent;
import android.content.SharedPreferences; // 추가됨
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText idEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // UI 연결
        idEditText = findViewById(R.id.edit_text_signup_id);
        passwordEditText = findViewById(R.id.edit_text_signup_password);
        nameEditText = findViewById(R.id.edit_text_signup_name);
        signupButton = findViewById(R.id.button_signup_confirm);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignupLocal(); // 로컬 회원가입 함수 호출
            }
        });
    }

    private void attemptSignupLocal() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();

        // 1. 빈칸 검사
        if (id.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 내부 저장소(SharedPreferences) 열기 - "UserDB"라는 이름의 가짜 DB
        SharedPreferences prefs = getSharedPreferences("UserDB", MODE_PRIVATE);

        // 3. 이미 있는 아이디인지 확인
        if (prefs.contains(id)) {
            Toast.makeText(this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. 저장하기 (회원가입 처리)
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(id, password); // 아이디를 키로, 비번을 값으로 저장
        editor.putString(id + "_name", name); // 이름도 저장
        editor.apply(); // 저장 완료

        // 5. 성공 메시지 및 화면 이동
        Toast.makeText(this, "회원가입 성공! 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 500);
    }
}