package com.example.kimkyeongbeom.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomHolder> {     //리싸이클러뷰 어탭터 클래스
    //    private ArrayList<Item> mItems;
    private Context mContext;
    ArrayList<Integer> mItems;

    CustomAdapter(ArrayList itemList) {
        mItems = itemList;
    }

    //View 생성, ViewHolder 호출
    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {      //뷰홀더 생성
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);    //아이템 레이아웃 inflate 하여 사용
        mContext = parent.getContext();
        CustomHolder holder = new CustomHolder(v);
        return holder;
    }

    //재활용되는 View 가 호출, Adapter 가 해당 position 에 해당하는 데이터를 결합
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {
        holder.tvRclTitle.setText(mItems.get(position) + "");
        holder.ivImg.setClipToOutline(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, holder.tvRclTitle.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }   //아이템 총 개수 반환


}
