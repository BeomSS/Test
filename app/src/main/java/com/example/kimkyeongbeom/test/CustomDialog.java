package com.example.kimkyeongbeom.test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class CustomDialog extends Dialog {
    Context context;
    String title, content;
    View.OnClickListener listener;
    TextView tvDlgTitle, tvDlgContent;
    Button btnDlgConfirm;

    CustomDialog(Context context, String title, String content, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.content = content;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = Objects.requireNonNull(getWindow()).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        tvDlgTitle = findViewById(R.id.tvDlgTitle);
        tvDlgContent = findViewById(R.id.tvDlgContent);
        btnDlgConfirm = findViewById(R.id.btnDlgConfirm);

        tvDlgTitle.setText(title);
        tvDlgContent.setText(content);
        btnDlgConfirm.setOnClickListener(listener);

    }

    @Override
    public void onBackPressed() {
        dismiss();
    }
}
