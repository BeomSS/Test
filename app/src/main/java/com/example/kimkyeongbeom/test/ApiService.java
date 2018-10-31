package com.example.kimkyeongbeom.test;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {
    //베이스 Url
    static final String BASEURL = "https://api2.sktelecom.com/";
    static final String APPKEY = "9a7fcfb8-5f41-496a-a768-731208381a4f";

    //get 메소드를 통한 http rest api 통신
    @GET("weather/current/hourly")
    Call<JsonObject> getHourly(@Header("appKey") String appKey, @Query("version") int version,
                               @Query("lat") double lat, @Query("lon") double lon);

}
