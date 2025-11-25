package com.cookandroid.caffeservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cookandroid.caffeservice.CafeData.CafeData;
import java.util.ArrayList;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> {

    private ArrayList<CafeData> mList;

    // 생성자
    public CafeAdapter(ArrayList<CafeData> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_cafe_list.xml 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cafe_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CafeData item = mList.get(position);

        // CafeData.java에 정의한 Getter 함수들 호출
        holder.tvName.setText(item.getName());
        holder.tvAddress.setText(item.getAddress());
        holder.tvRating.setText("★ " + item.getRating());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    // 뷰홀더 클래스
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_cafe_list.xml에 있는 ID들과 연결
            tvName = itemView.findViewById(R.id.tvCafeName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvRating = itemView.findViewById(R.id.tvRating);
        }
    }
}