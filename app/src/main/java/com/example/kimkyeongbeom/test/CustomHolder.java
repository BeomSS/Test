package com.example.kimkyeongbeom.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomHolder extends RecyclerView.ViewHolder {
    public TextView tvRclTitle;
    public ImageView ivImg;

    CustomHolder(View itemView) {
        super(itemView);
        tvRclTitle = itemView.findViewById(R.id.tvRclTitle);
        ivImg = itemView.findViewById(R.id.ivImg);
    }
}
