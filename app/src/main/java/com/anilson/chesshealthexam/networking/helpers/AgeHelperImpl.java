package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.apis.AgeService;
import com.anilson.chesshealthexam.networking.models.AgeResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public class AgeHelperImpl implements AgeHelper {

    private final AgeService ageApi;

    @Inject
    public AgeHelperImpl(AgeService ageApi) {
        this.ageApi = ageApi;
    }

    @Override
    public Observable<Response<AgeResponse>> getAge(String name) {
        return ageApi.getAge(name);
    }
}
