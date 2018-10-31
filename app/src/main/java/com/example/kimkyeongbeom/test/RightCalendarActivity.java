package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RightCalendarActivity extends Activity {
    public static Context context;
    TextView tvTitle, tvContent;
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        tvTitle.setText("");
        tvContent.setText("");

        dbHelper = new DBHelper(this);
        sqlDB = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqlDB);

        final Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedDay = ((CustomCalendarView) CustomCalendarView.layout).selectedDayStr;
                Cursor cursor = sqlDB.rawQuery("select count(*) from diary where date is '" + selectedDay + "';", null);
                cursor.moveToNext();
                if (cursor.getInt(0) <= 0) {
                    Toast.makeText(RightCalendarActivity.this, "저장된 일정이 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    sqlDB.execSQL("delete from diary where date is '" + selectedDay + "';");
                    Toast.makeText(RightCalendarActivity.this, "삭제했습니다.", Toast.LENGTH_SHORT).show();
                    ((CustomCalendarView) CustomCalendarView.layout).updateCalendar();
                    tvTitle.setText("");
                    tvContent.setText("");
                }
                cursor.close();
            }
        });
        context = this;
    }

    @Override
    public void onBackPressed() {
        sqlDB.close();
        finish();
        overridePendingTransition(R.anim.translate_stop, R.anim.translate_to_right);
    }
}
