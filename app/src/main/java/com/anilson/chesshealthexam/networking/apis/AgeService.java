package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.AgeResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AgeService {

    @GET("/")
    Observable<Response<AgeResponse>> getAge(@Query("name") String name);
}
