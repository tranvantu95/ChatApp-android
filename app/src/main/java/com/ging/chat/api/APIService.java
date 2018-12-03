package com.ging.chat.api;

import com.ging.chat.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/api/login")
    @FormUrlEncoded
    public Call<LoginResponse> login(@Field("username") String account,
                                     @Field("password") String password);

    @POST("/api/register")
    @FormUrlEncoded
    public Call<LoginResponse> register(@Field("username") String account,
                                     @Field("password") String password);

}
