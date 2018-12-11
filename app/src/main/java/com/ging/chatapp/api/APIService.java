package com.ging.chatapp.api;

import com.ging.chatapp.api.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/api/login")
    @FormUrlEncoded
    public Call<LoginResponse> login(@Field("account") String account,
                                     @Field("password") String password);

    @POST("/api/register/fast")
    @FormUrlEncoded
    public Call<LoginResponse> register(@Field("account") String account,
                                     @Field("password") String password);

    @POST("/api/login/facebook")
    @FormUrlEncoded
    public Call<LoginResponse> loginFacebook(@Field("access_token") String accessToken);

}
