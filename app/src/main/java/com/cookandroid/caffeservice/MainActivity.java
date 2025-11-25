package com.cookandroid.caffeservice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

// 구글 지도 관련 임포트
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // ==========================================
    // 1. 변수 선언
    // ==========================================

    // UI 컴포넌트
    private CardView cardSearchBar;
    private ImageView btnHome, btnFavorite, btnSearch, btnMyPage; // 탭 버튼

    // 구글 지도 객체
    private GoogleMap mMap;

    // 프래그먼트 관리
    private FragmentManager fragmentManager;
    private int currentSelectedTab = R.id.btnHome; // 초기 선택 탭

    // 색상 변수 (XML colors.xml에서 가져옴)
    private int colorPink;
    private int colorGray;

    // 별점 시스템 (현재 XML엔 없어서 null 체크 필요)
    private RatingBar ratingBar;
    private TextView ratingDisplayTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ==========================================
        // 2. 초기화 작업
        // ==========================================

        // 2-1. 색상 리소스 로드
        colorPink = ContextCompat.getColor(this, R.color.icon_color); // #DD7793
        colorGray = ContextCompat.getColor(this, R.color.gray);       // #CACACA

        // 2-2. 뷰 연결
        cardSearchBar = findViewById(R.id.cardSearchBar);
        btnHome = findViewById(R.id.btnHome);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnSearch = findViewById(R.id.btnSearch);
        btnMyPage = findViewById(R.id.btnMyPage);

        // 2-3. 구글 지도 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 2-4. 프래그먼트 매니저 초기화
        fragmentManager = getSupportFragmentManager();


        // ==========================================
        // 3. 리스너(이벤트) 설정
        // ==========================================

        // 검색창 클릭 이벤트
        cardSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 검색 화면(SearchActivity)으로 이동
                Toast.makeText(MainActivity.this, "검색 화면 이동", Toast.LENGTH_SHORT).show();
            }
        });

        // 하단 탭 버튼 클릭 리스너 정의
        View.OnClickListener tabListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedId = v.getId();

                // 1. 탭 아이콘 색상 변경
                updateTabIcons(clickedId);

                // 2. 프래그먼트 전환
                Fragment selectedFragment = null;

                if (clickedId == R.id.btnHome) {
                    selectedFragment = new MapFragment();
                } else if (clickedId == R.id.btnFavorite) {
                    selectedFragment = new FavoriteFragment();
                } else if (clickedId == R.id.btnSearch) {
                    selectedFragment = new SearchFragment();
                } else if (clickedId == R.id.btnMyPage) {
                    selectedFragment = new MyPageFragment();
                }

                // loadFragment(selectedFragment, clickedId);
                // 현재 XML 구조는 지도가 배경이라 프래그먼트 컨테이너가 따로 필요할 수 있음.
                // 일단 색상 변경만 우선 적용.
            }
        };

        // 리스너 등록
        btnHome.setOnClickListener(tabListener);
        btnFavorite.setOnClickListener(tabListener);
        btnSearch.setOnClickListener(tabListener);
        btnMyPage.setOnClickListener(tabListener);

        // 초기 상태: 홈 버튼 활성화
        updateTabIcons(R.id.btnHome);


        // ==========================================
        // 4. 별점 로직 (null 체크 추가)
        // ==========================================
        ratingBar = findViewById(R.id.starRatingBar); // 현재 XML엔 없음 (앱 안 꺼지게 null 체크 함)
        ratingDisplayTextView = findViewById(R.id.ratingDisplayTextView);

        if (ratingBar != null) {
            updateRatingText(ratingBar.getRating());
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    updateRatingText(rating);
                    if (fromUser) {
                        handleFinalRating((int) rating);
                        Toast.makeText(MainActivity.this, "선택된 별점: " + (int) rating, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    } // onCreate 끝


    // ==========================================
    // 5. 구글 지도 준비 완료 (Callback)
    // ==========================================
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 동양미래대학교 좌표
        LatLng dongyangUniv = new LatLng(37.499990, 126.867580);

        // 마커 추가
        mMap.addMarker(new MarkerOptions()
                .position(dongyangUniv)
                .title("동양미래대학교"));

        // 카메라 이동 (줌 레벨 17)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dongyangUniv, 17f));

        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            // 권한이 없을 경우 처리
        }
    }


    // ==========================================
    // 6. 헬퍼 함수들 (UI 업데이트 등)
    // ==========================================

    /**
     * 선택된 탭은 핑크색, 나머지는 회색으로 변경하는 함수
     */
    private void updateTabIcons(int selectedId) {
        btnHome.setColorFilter(selectedId == R.id.btnHome ? colorPink : colorGray);
        btnFavorite.setColorFilter(selectedId == R.id.btnFavorite ? colorPink : colorGray);
        btnSearch.setColorFilter(selectedId == R.id.btnSearch ? colorPink : colorGray);
        btnMyPage.setColorFilter(selectedId == R.id.btnMyPage ? colorPink : colorGray);

        currentSelectedTab = selectedId;
    }

    /**
     * 프래그먼트 교체 함수
     * 주의: activity_main.xml에 FrameLayout 컨테이너가 있어야 정상 작동함
     */
    private void loadFragment(Fragment fragment, int newTabId) {
        if (newTabId == currentSelectedTab) return;

        // XML에 교체할 컨테이너 ID(예: R.id.fragment_container)가 필요함
        // FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.replace(R.id.fragment_container, fragment);
        // transaction.commit();

        currentSelectedTab = newTabId;
    }

    // 별점 텍스트 업데이트
    private void updateRatingText(float rating) {
        if (ratingDisplayTextView != null) {
            String formattedRating = String.format("%.1f 점", rating);
            ratingDisplayTextView.setText(formattedRating);
        }
    }

    // 최종 별점 처리
    private void handleFinalRating(int score) {
        System.out.println("DEBUG: 서버로 최종 별점 " + score + "점 전송 준비 완료.");
    }

    public void replaceFragment(com.cookandroid.caffeservice.SearchFragment searchFragment) {
    }

    // ==========================================
    // 7. 프래그먼트 클래스
    // ==========================================
    public static class MapFragment extends Fragment {}
    public static class FavoriteFragment extends Fragment {}
    public static class SearchFragment extends Fragment {}
    public static class MyPageFragment extends Fragment {}
}