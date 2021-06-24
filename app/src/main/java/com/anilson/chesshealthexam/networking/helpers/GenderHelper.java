package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.models.GenderResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public interface GenderHelper {

    Observable<Response<GenderResponse>> getGender(String name);
}
