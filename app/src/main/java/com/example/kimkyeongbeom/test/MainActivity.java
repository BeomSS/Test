package com.example.kimkyeongbeom.test;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Button btnLeft, btnTop, btnRight, btnBottom;
    NavigationView navigationView;
    CustomDialog dlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.loDrawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        btnLeft = findViewById(R.id.btnLeft);
        btnTop = findViewById(R.id.btnTop);
        btnRight = findViewById(R.id.btnRight);
        btnBottom = findViewById(R.id.btnBottom);
        navigationView = findViewById(R.id.nav_main);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int drawerLockMode = drawer.getDrawerLockMode(GravityCompat.START);
                if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.navItem1) {
                    Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navItem2) {
                    Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navItem3) {
                    Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.navItem4) {
                    Toast.makeText(MainActivity.this, "4", Toast.LENGTH_SHORT).show();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TopActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_top_to_center, R.anim.translate_stop);
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RightActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.translate_right_to_center, R.anim.translate_stop);
            }
        });
        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new CustomDialog(MainActivity.this, "샘플입니다", "샘플입니다", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }
    }
}


