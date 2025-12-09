package com.cookandroid.caffeservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

public class MypageFragment extends Fragment {

    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "currentUserId";
    private LinearLayout btnMyReview;
    private LinearLayout btnLogout;

    private TextView nicknameTextView;
    private ImageView settingsIcon;
    private RecyclerView rvTimelineFeed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mypage, container, false);

        // 1. 닉네임 설정
        nicknameTextView = rootView.findViewById(R.id.nickname_text);
        updateNicknameDisplay();

        // 2. 설정 아이콘
        settingsIcon = rootView.findViewById(R.id.settings_icon);
        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(v -> showSettingsDialog());
        }

        // 3. 내 리뷰 버튼 (ID: btn_my_review)
        btnMyReview = rootView.findViewById(R.id.btn_my_review);
        if (btnMyReview != null) {
            btnMyReview.setOnClickListener(v -> {
                // 내 리뷰 클릭 시 아무 동작 안 함
                // Toast.makeText(getContext(), "내 리뷰 화면 준비 중", Toast.LENGTH_SHORT).show();
            });
        }

        // 4. 로그아웃 버튼 (ID: btn_logout)
        btnLogout = rootView.findViewById(R.id.btn_logout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                performLogout(); // 로그아웃 메서드 호출
            });
        }

        // 5. 타임라인 리스트 초기화
        rvTimelineFeed = rootView.findViewById(R.id.rvTimelineFeed);
        if (rvTimelineFeed != null) {
            initTimelineRecyclerView();
        }

        return rootView;
    }

    private void updateNicknameDisplay() {
        Context context = getContext();
        if (context != null && nicknameTextView != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString(KEY_USER_ID, "로그인 사용자");
            nicknameTextView.setText(userId + " 님");
        }
    }

    private void showSettingsDialog() {
        if (getContext() == null) return;
        final String[] items = {"내 정보 수정", "탈퇴하기", "버전 정보"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("설정 메뉴");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:
                    Toast.makeText(getContext(), "내 정보 수정 페이지로 이동 예정", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    showWithdrawalConfirmationDialog();
                    break;
                case 2:
                    showAppVersionDialog();
                    break;
            }
        });
        builder.show();
    }

    private void showAppVersionDialog() {
        if (getContext() == null) return;
        String appVersion;
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = "버전 정보를 찾을 수 없습니다.";
        }
        new AlertDialog.Builder(getContext())
                .setTitle("버전 정보")
                .setMessage("현재 버전: v" + appVersion + "\n\n(c) 2025 CafeService Team")
                .setPositiveButton("확인", null)
                .show();
    }

    private void showWithdrawalConfirmationDialog() {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("경고: 정말 탈퇴하시겠습니까?")
                .setMessage("탈퇴 시 모든 사용자 정보 및 리뷰 데이터가 영구적으로 삭제됩니다. 계속하시겠습니까?")
                .setPositiveButton("예 (탈퇴)", (dialog, which) -> {
                    Toast.makeText(getContext(), "탈퇴 처리 진행 중...", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    private void performLogout() {
        Context context = getContext();
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.remove(KEY_USER_ID);
            editor.apply();

            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void initTimelineRecyclerView() {
        ArrayList<TimelineData> timelineList = new ArrayList<>();
        timelineList.add(new TimelineData("카페 피드", "서울시 마포구 동교동", "2025-11-25"));
        timelineList.add(new TimelineData("더존매터", "서울시 마포구 성미산로", "2025-11-23"));
        timelineList.add(new TimelineData("카페드레브", "서울시 강남구 테헤란로", "2025-11-20"));
        timelineList.add(new TimelineData("쿠이케", "서울시 종로구 삼청동", "2025-11-18"));

        // TimelineAdapter가 없으면 오류가 날 수 있으니 파일이 있는지 확인하세요.
        TimelineAdapter timelineAdapter = new TimelineAdapter(timelineList);
        rvTimelineFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimelineFeed.setAdapter(timelineAdapter);
    }
}