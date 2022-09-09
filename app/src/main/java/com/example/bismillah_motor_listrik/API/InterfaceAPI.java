package com.example.bismillah_motor_listrik.API;

import com.example.bismillah_motor_listrik.model.Login;
import com.example.bismillah_motor_listrik.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterfaceAPI {

    String BASE_URL = "https://7bd4-125-166-12-37.ap.ngrok.io/";

    @POST("api/login")
    @FormUrlEncoded
    Call<Login> loginScanner(@Field("username") String username);

    @GET("api/user/{id}")
    Call<User> user(@Path("id") String id);

}
