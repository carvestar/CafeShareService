package com.cookandroid.caffeservice; // 실제 패키지명으로 변경해주세요

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView; // 카페 이름 표시를 위해 추가
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    // UI 컴포넌트 변수 선언 (단순화: 사진, 해시태그 관련 컴포넌트 제거)
    private RatingBar ratingBar;
    private EditText reviewEditText;
    private Button registerButton;
    private TextView cafeNameTextView; // 카페 이름 표시 TextView

    // 현재 리뷰를 작성할 카페 ID 및 이름
    private String cafeId;
    private String cafeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XML 레이아웃 파일(activity_review.xml)을 화면에 연결
        setContentView(R.layout.activity_review);

        // 0. Intent 데이터 수신 (리뷰 대상 카페 정보)
        Intent intent = getIntent();
        cafeId = intent.getStringExtra("CAFE_ID");
        cafeName = intent.getStringExtra("CAFE_NAME");


        // 1. UI 컴포넌트 연결 (필요한 최소한의 요소만 연결)
        ratingBar = findViewById(R.id.rating_bar);
        reviewEditText = findViewById(R.id.edit_text_review);
        registerButton = findViewById(R.id.button_register);
        cafeNameTextView = findViewById(R.id.cafe_name_title); // XML에 tv_cafe_name ID가 있다고 가정

        // 1-1. 카페 이름 표시 업데이트
        if (cafeName != null && cafeNameTextView != null) {
            // 만약 cafeName이 null이면 "리뷰 작성"만 표시
            cafeNameTextView.setText(cafeName + " 리뷰 작성");
        } else if (cafeNameTextView != null) {
            cafeNameTextView.setText("리뷰 작성");
        }


        // 2. 등록 버튼 클릭 리스너 (핵심 로직)
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });

        // 3. RatingBar 변경 리스너 (선택 사항: 별점 선택 시 즉시 피드백)
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                Log.d(TAG, "Selected rating: " + rating);
            }
        });
    }

    /**
     * 입력된 리뷰 데이터(별점, 글 내용)를 수집하고 백엔드 API를 호출하여 등록하는 메서드입니다.
     */
    private void submitReview() {
        float rating = ratingBar.getRating();
        String reviewText = reviewEditText.getText().toString().trim();

        // 1. 필수 입력 항목 검사
        if (rating == 0.0f) {
            Toast.makeText(this, "별점을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "리뷰 내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 0. 리뷰 대상 카페 ID 확인 (필수)
        if (cafeId == null || cafeId.isEmpty()) {
            Toast.makeText(this, "리뷰할 카페 정보가 누락되었습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. 데이터 로그 출력 (디버깅용)
        Log.d(TAG, "Review submitted for Cafe ID: " + cafeId);
        Log.d(TAG, "Rating: " + rating);
        Log.d(TAG, "Review Text: " + reviewText);

        // 3. 백엔드 통신 로직 (TODO: 여기에 실제 API 호출 코드를 추가합니다.)
        // 예: ReviewApi.submitReview(cafeId, rating, reviewText, ...);

        // 임시 성공 처리
        Toast.makeText(this, "리뷰 등록 시도 중... (별점: " + rating + "점)", Toast.LENGTH_LONG).show();

        // 리뷰 등록 성공 후 화면 닫기 (주석 해제 후 사용)
        // finish();
    }
}