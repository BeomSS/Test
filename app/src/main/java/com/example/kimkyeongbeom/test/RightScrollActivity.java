package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class RightScrollActivity extends Activity {
    RecyclerView rclView;
    LinearLayoutManager mLayoutManager;
    CustomAdapter adapter;
    Boolean isScrolling = false;
    ArrayList<Integer> items;
    int currentItems, totalItems, scrollOutItems, cnt = 0;
    ProgressBar pbLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        rclView = findViewById(R.id.rclView);
        mLayoutManager = new LinearLayoutManager(this);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.GONE);

        items = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            items.add(i);
        }
        adapter = new CustomAdapter(items);
        rclView.setLayoutManager(mLayoutManager);
        rclView.setAdapter(adapter);

        rclView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    //data fetch
                    isScrolling = false;
                    fetchData();
                }
            }
        });

    }

    private void fetchData() {
        cnt = items.size() + 1;
        if (cnt <= 50) {
            pbLoading.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = cnt; i < cnt + 10; i++) {
                        items.add(i);
                        adapter.notifyDataSetChanged();
                        pbLoading.setVisibility(View.GONE);
                    }
                }
            }, 500);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_stop, R.anim.translate_to_right);
    }
}
