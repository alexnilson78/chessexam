package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.AgeResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AgeApi {

    @GET("/")
    Response<AgeResponse> getAge(@Query("name") String name);
}
