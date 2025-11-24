package com.cookandroid.caffeservice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;
import com.cookandroid.caffeservice.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

// Google 로그인 관련 Import
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

// Retrofit 및 데이터 모델 Import
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.cookandroid.caffeservice.api.RetrofitClient;
import com.cookandroid.caffeservice.LoginData.LoginRequest;
import com.cookandroid.caffeservice.LoginData.LoginResponse;
import com.cookandroid.caffeservice.SignupActivity;
import com.cookandroid.caffeservice.LoginData.GoogleLoginRequest;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpText;
    private ImageButton googleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. UI 요소 초기화 및 연결
        idEditText = findViewById(R.id.edit_text_id);
        passwordEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);
        signUpText = findViewById(R.id.text_signup);
        googleLoginButton = findViewById(R.id.button_google_login);

        // 2. GoogleSignInOptions 설정 및 클라이언트 객체 생성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 3. Google 로그인 버튼 클릭 리스너 설정
        googleLoginButton.setOnClickListener(v -> signInWithGoogle());

        // 4. 일반 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 5. 회원가입 텍스트 클릭 리스너 설정
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // -----------------------------------------------------
    // ⭐️ 화면 전환 유틸리티 메서드 ⭐️
    // -----------------------------------------------------

    private void navigateToMain(String token) {
        // ⚠️ TODO: 여기서 토큰(token)을 SharedPreferences 등에 저장해야 합니다. ⚠️
        Toast.makeText(this, "로그인 성공! 메인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // 로그인 화면을 스택에서 제거하여 뒤로가기 버튼으로 돌아오지 않게 합니다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // finish(); // FLAG_ACTIVITY_CLEAR_TASK를 사용하면 finish()를 명시하지 않아도 됩니다.
    }


    // -----------------------------------------------------
    // ⭐️ Google 로그인 로직 ⭐️
    // -----------------------------------------------------

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            sendTokenToServer(idToken);

        } catch (ApiException e) {
            Log.w(TAG, "Google sign in failed: " + e.getStatusCode(), e);
            Toast.makeText(this, "Google 로그인 실패: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTokenToServer(String token) {
        RetrofitClient.getAuthService().googleLogin(new GoogleLoginRequest(token))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isSuccess()) {
                                // ⭐️ 1. Google 로그인 성공 시 MainActivity로 이동 ⭐️
                                String accessToken = loginResponse.getAccessToken();
                                navigateToMain(accessToken);
                            } else {
                                Toast.makeText(LoginActivity.this, "Google 로그인 실패: 서버 처리 오류", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Google 로그인 실패: 서버 처리 오류", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Server error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "API Call Failed: " + t.getMessage(), t);
                    }
                });
    }

    // -----------------------------------------------------
    // ⭐️ 일반 로그인 로직 ⭐️
    // -----------------------------------------------------

    private void attemptLogin() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Login Attempt with ID: " + id);

        LoginRequest requestBody = new LoginRequest(id, password);

        RetrofitClient.getAuthService().login(requestBody).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse != null && loginResponse.isSuccess()) {
                        // ⭐️ 2. 일반 로그인 성공 시 MainActivity로 이동 ⭐️
                        String accessToken = loginResponse.getAccessToken();
                        navigateToMain(accessToken);

                    } else {
                        String errorMessage = (loginResponse != null && loginResponse.getMessage() != null) ?
                                loginResponse.getMessage() : "로그인에 실패했습니다. (서버 응답 오류)";
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: 서버 응답 오류 (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN_API", "API Call Failed: " + t.getMessage(), t);
            }
        });
    }
}