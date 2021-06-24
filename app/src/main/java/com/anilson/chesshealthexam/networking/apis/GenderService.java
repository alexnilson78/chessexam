package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.GenderResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GenderService {

    @GET("/")
    Observable<Response<GenderResponse>> getGender(@Query("name") String name);
}
