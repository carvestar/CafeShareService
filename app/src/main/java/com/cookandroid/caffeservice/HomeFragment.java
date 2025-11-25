package com.cookandroid.caffeservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_home.xml 연결
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 지도 프래그먼트 불러오기 (자식 프래그먼트)
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 검색바 클릭 이벤트 (MainActivity의 검색바와 같은 기능)
        View searchBar = view.findViewById(R.id.cardSearchBar);
        searchBar.setOnClickListener(v -> {
            // 여기에 검색 화면으로 이동하는 코드를 넣거나, MainActivity의 기능을 호출
            // 예: ((MainActivity)getActivity()).replaceFragment(new SearchFragment());
            Toast.makeText(getContext(), "검색 화면으로 이동", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 동양미래대학교 좌표
        LatLng dongyangUniv = new LatLng(37.499990, 126.867580);

        // 마커 추가
        mMap.addMarker(new MarkerOptions()
                .position(dongyangUniv)
                .title("동양미래대학교"));

        // 카메라 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dongyangUniv, 17f));

        // 내 위치 활성화 (권한 필요)
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) { }
    }
}