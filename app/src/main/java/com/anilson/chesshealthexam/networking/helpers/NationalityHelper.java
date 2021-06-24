package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public interface NationalityHelper {

    Observable<Response<NationalityResponse>> getNationality(String name);
}
