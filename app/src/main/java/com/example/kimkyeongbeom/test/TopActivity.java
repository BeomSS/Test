package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class TopActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_stop, R.anim.translate_to_top);
        //Test
        //asdasdsa
        //ㅁㄴㅇㄴㅁㅇㅁㄴㅇㅁㄴㅇ
    }
}
