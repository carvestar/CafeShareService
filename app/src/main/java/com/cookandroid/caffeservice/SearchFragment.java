package com.cookandroid.caffeservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cookandroid.caffeservice.CafeData.CafeData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchFragment extends Fragment {

    // 변수 선언
    private EditText etSearch;
    private Spinner spinnerSort;

    private ImageView btnSearch;
    private RecyclerView recyclerView;

    private ArrayList<CafeData> cafeList;
    private CafeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.etSearchKeyword);
        btnSearch = view.findViewById(R.id.btnSearch);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        recyclerView = view.findViewById(R.id.rvSearchResult);

        // 데이터 초기화 (더미 데이터)
        cafeList = new ArrayList<>();
        cafeList.add(new CafeData("카페 핀드", "마포구 동교로", 4.5, 150, 0, 0));
        cafeList.add(new CafeData("더존매터", "마포구 성미산로", 4.2, 300, 0, 0));
        cafeList.add(new CafeData("쿠이케", "마포구 월드컵로", 4.8, 600, 0, 0));

        // 리사이클러뷰 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CafeAdapter(cafeList);
        recyclerView.setAdapter(adapter);

        // 정렬 스피너 설정
        String[] sortOptions = {"거리순", "평점순"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, sortOptions);
        spinnerSort.setAdapter(spinnerAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortListByDistance();
                } else {
                    sortListByRating();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    // 거리순 정렬 (오름차순)
    private void sortListByDistance() {
        Collections.sort(cafeList, new Comparator<CafeData>() {
            @Override
            public int compare(CafeData o1, CafeData o2) {
                return Double.compare(o1.getDistance(), o2.getDistance());
            }
        });
        adapter.notifyDataSetChanged();
    }

    // 평점순 정렬 (내림차순)
    private void sortListByRating() {
        Collections.sort(cafeList, new Comparator<CafeData>() {
            @Override
            public int compare(CafeData o1, CafeData o2) {
                return Double.compare(o2.getRating(), o1.getRating());
            }
        });
        adapter.notifyDataSetChanged();
    }
}