package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NationalityApi {

    @GET("/")
    Response<NationalityResponse> getNationality(@Query("name") String name);
}
