package com.cookandroid.caffeservice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    // ==========================================
    // 1. 변수 선언
    // ==========================================

    // 탭 버튼
    private ImageView btnHome, btnFavorite, btnSearch, btnMyPage;

    // 현재 선택된 탭 ID
    private int currentSelectedTab = R.id.btnHome;

    // 색상 변수
    private int colorPink;
    private int colorGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ==========================================
        // 2. 초기화 작업
        // ==========================================

        // 2-1. 색상 리소스 로드
        colorPink = ContextCompat.getColor(this, R.color.icon_color);
        colorGray = ContextCompat.getColor(this, R.color.gray);

        // 2-2. 탭 버튼 연결
        btnHome = findViewById(R.id.btnHome);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnSearch = findViewById(R.id.btnSearch);
        btnMyPage = findViewById(R.id.btnMyPage);

        // 2-3. 앱 실행 시 초기 화면으로 '지도(MapFragment)' 설정
        if (savedInstanceState == null) {
            loadFragment(new MapFragment());
            updateTabIcons(R.id.btnHome);
        }

        // ==========================================
        // 3. 리스너(이벤트) 설정
        // ==========================================
        View.OnClickListener tabListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedId = v.getId();

                // 이미 선택된 탭이면 무시
                if (clickedId == currentSelectedTab) return;

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

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
            }
        };

        btnHome.setOnClickListener(tabListener);
        btnFavorite.setOnClickListener(tabListener);
        btnSearch.setOnClickListener(tabListener);
        btnMyPage.setOnClickListener(tabListener);
    }

    // ==========================================
    // 4. 헬퍼 함수들
    // ==========================================

    private void updateTabIcons(int selectedId) {
        btnHome.setColorFilter(selectedId == R.id.btnHome ? colorPink : colorGray);
        btnFavorite.setColorFilter(selectedId == R.id.btnFavorite ? colorPink : colorGray);
        btnSearch.setColorFilter(selectedId == R.id.btnSearch ? colorPink : colorGray);
        btnMyPage.setColorFilter(selectedId == R.id.btnMyPage ? colorPink : colorGray);

        currentSelectedTab = selectedId;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // activity_main.xml의 FrameLayout ID
        transaction.commit();
    }

    // ==========================================
    // 5. 프래그먼트 클래스들 (Inner Class)
    // ==========================================

    /**
     * 지도와 검색바를 보여주는 메인 프래그먼트
     */
    public static class MapFragment extends Fragment implements OnMapReadyCallback {

        private GoogleMap mMap;
        private CardView cardSearchBar;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // fragment_home.xml 레이아웃 인플레이트
            View view = inflater.inflate(R.layout.fragment_home, container, false);

            // 검색바 클릭 이벤트 설정
            cardSearchBar = view.findViewById(R.id.cardSearchBar);
            cardSearchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 검색 화면이나 액티비티로 이동 구현
                    Toast.makeText(getContext(), "검색 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                }
            });

            // 지도 초기화 (ChildFragmentManager 사용 중요!)
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);

            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }

            return view;
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;

            // 동양미래대학교 좌표
            LatLng dongyangUniv = new LatLng(37.499990, 126.867580);

            mMap.addMarker(new MarkerOptions()
                    .position(dongyangUniv)
                    .title("동양미래대학교"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dongyangUniv, 17f));

            // 권한 체크 및 내 위치 활성화
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // 권한 요청
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
            }
        }
    }

    // 빈 프래그먼트들 (나중에 구현)
    public static class FavoriteFragment extends Fragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // 임시로 빈 화면 반환 (배경색 등으로 구분 가능)
            View v = new View(getContext());
            v.setBackgroundColor(0xFFEEEEEE);
            return v;
        }
    }
    public static class SearchFragment extends Fragment {}
    public static class MyPageFragment extends Fragment {}
}