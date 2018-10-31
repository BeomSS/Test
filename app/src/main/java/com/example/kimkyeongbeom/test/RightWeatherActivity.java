package com.example.kimkyeongbeom.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RightWeatherActivity extends Activity {
    TextView tvLat, tvLng, tvAddress, tvProvider, tvTemp, tvTempMax, tvTempMin, tvSky;
    ProgressBar pgbLat, pgbLng, pgbAddress, pgbProvider, pgbTemp, pgbTempMax, pgbTempMin, pgbSky;
    LocationManager manager;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLng = (TextView) findViewById(R.id.tvLng);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvProvider = (TextView) findViewById(R.id.tvProvider);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvTempMax = (TextView) findViewById(R.id.tvTempMax);
        tvTempMin = (TextView) findViewById(R.id.tvTempMin);
        tvSky = (TextView) findViewById(R.id.tvSky);
        pgbLat = (ProgressBar) findViewById(R.id.pgbLat);
        pgbLng = (ProgressBar) findViewById(R.id.pgbLng);
        pgbAddress = (ProgressBar) findViewById(R.id.pgbAddress);
        pgbProvider = (ProgressBar) findViewById(R.id.pgbProvider);
        pgbTemp = (ProgressBar) findViewById(R.id.pgbTemp);
        pgbTempMax = (ProgressBar) findViewById(R.id.pgbTempMax);
        pgbTempMin = (ProgressBar) findViewById(R.id.pgbTempMin);
        pgbSky = (ProgressBar) findViewById(R.id.pgbSky);
        mContext = this;
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) { //위치 권한묻기
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 0);
            } else {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
            }
        } catch (SecurityException e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {
            Log.d("test", "onLocationChanged, location:" + location);
            double latitude = location.getLatitude(); //경도
            double longitude = location.getLongitude();   //위도
            String provider = location.getProvider();   //위치제공자

            tvLat.setText(tvLat.getText().toString() + latitude);
            pgbLat.setVisibility(View.GONE);
            tvLng.setText(tvLng.getText().toString() + longitude);
            pgbLng.setVisibility(View.GONE);
            tvProvider.setText(tvProvider.getText().toString() + provider);
            pgbProvider.setVisibility(View.GONE);

            Geocoder geo = new Geocoder(mContext);
            List<Address> list = null;
            try {
                list = geo.getFromLocation(latitude, longitude, 5);
            } catch (IOException e) {
                Toast.makeText(mContext, "위치정보 오류", Toast.LENGTH_SHORT).show();
            }
            if (list == null) {
                Toast.makeText(mContext, "주소정보 없음", Toast.LENGTH_SHORT).show();
            } else {
                Address address = list.get(0);
                tvAddress.setText(tvAddress.getText().toString() + address.getAddressLine(0));
                pgbAddress.setVisibility(View.GONE);
            }

            getWeather(latitude, longitude);

            manager.removeUpdates(mLocationListener);
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASEURL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getHourly(ApiService.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    JsonObject object = response.body();
                    if (object != null) {
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        JsonObject weatherObject = object.get("weather").getAsJsonObject().get("hourly").getAsJsonArray().get(0).getAsJsonObject();
                        JsonElement temp, sky;
                        temp = weatherObject.get("temperature");
                        sky = weatherObject.get("sky");
                        String strTemp, strTempMax, strTempMin, strSky;
                        strTemp = temp.getAsJsonObject().get("tc").toString().replaceAll("\"", "");
                        strTempMax = temp.getAsJsonObject().get("tmax").toString().replaceAll("\"", "");
                        strTempMin = temp.getAsJsonObject().get("tmin").toString().replaceAll("\"", "");
                        strSky = sky.getAsJsonObject().get("name").toString().replaceAll("\"", "");
                        tvTemp.setText(tvTemp.getText().toString() + String.format("%.1f ℃", Float.parseFloat(strTemp)));
                        pgbTemp.setVisibility(View.GONE);
                        tvTempMax.setText(tvTempMax.getText().toString() + String.format("%.1f ℃", Float.parseFloat(strTempMax)));
                        pgbTempMax.setVisibility(View.GONE);
                        tvTempMin.setText(tvTempMin.getText().toString() + String.format("%.1f ℃", Float.parseFloat(strTempMin)));
                        pgbTempMin.setVisibility(View.GONE);
                        tvSky.setText(tvSky.getText().toString() + strSky);
                        pgbSky.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Toast.makeText(RightWeatherActivity.this, "정보 가져오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.translate_stop, R.anim.translate_to_right);
    }
}
