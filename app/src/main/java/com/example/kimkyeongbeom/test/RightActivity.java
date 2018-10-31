package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class RightActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right);
        Button btnScroll, btnWhether, btnCalnedar;
        btnScroll = findViewById(R.id.btnScroll);
        btnWhether = findViewById(R.id.btnWhether);
        btnCalnedar = findViewById(R.id.btnCalendar);

        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RightActivity.this, RightScrollActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_right_to_center, R.anim.translate_stop);
            }
        });
        btnWhether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RightActivity.this, RightWeatherActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_right_to_center, R.anim.translate_stop);
            }
        });
        btnCalnedar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RightActivity.this, RightCalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_right_to_center, R.anim.translate_stop);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_stop, R.anim.translate_to_right);
    }

}