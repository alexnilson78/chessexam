package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.models.AgeResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public interface AgeHelper {
    Observable<Response<AgeResponse>> getAge(String name);
}
