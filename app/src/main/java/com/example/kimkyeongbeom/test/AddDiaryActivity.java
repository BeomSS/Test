package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddDiaryActivity extends Activity {
    TextView tvAddDay;
    EditText edtAddTitle, edtAddContent;
    Button btnAddCancel, btnAddSave;
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    String day, title, content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        tvAddDay = findViewById(R.id.tvAddDay);
        edtAddTitle = findViewById(R.id.edtAddTitle);
        edtAddContent = findViewById(R.id.edtAddContent);
        btnAddCancel = findViewById(R.id.btnAddCancel);
        btnAddSave = findViewById(R.id.btnAddSave);

        dbHelper = new DBHelper(this);
        sqlDB = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        day = intent.getStringExtra("addDay");
        tvAddDay.setText(day);

        btnAddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = edtAddTitle.getText().toString();
                content = edtAddContent.getText().toString();
                SQLiteStatement stmt = sqlDB.compileStatement("insert into diary values(?, ?, ?);");
                stmt.bindString(1, day);
                stmt.bindString(2, title);
                stmt.bindString(3, content);
                stmt.execute();
                Toast.makeText(AddDiaryActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                ((CustomCalendarView) CustomCalendarView.layout).updateCalendar();
                ((RightCalendarActivity) RightCalendarActivity.context).tvTitle.setText(title);
                ((RightCalendarActivity) RightCalendarActivity.context).tvContent.setText(content);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
