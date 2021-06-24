package com.anilson.chesshealthexam.networking.apis;

import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NationalityService {

    @GET("/")
    Observable<Response<NationalityResponse>> getNationality(@Query("name") String name);
}
