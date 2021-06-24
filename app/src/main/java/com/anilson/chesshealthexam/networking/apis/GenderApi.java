package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.GenderResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenderApi {

    @GET("/")
    Response<GenderResponse> getGender(@Query("name") String name);
}
