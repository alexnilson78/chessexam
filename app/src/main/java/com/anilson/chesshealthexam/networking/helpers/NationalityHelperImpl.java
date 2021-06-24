package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.models.NationalityResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public class NationalityHelperImpl implements NationalityHelper {

    private final NationalityService nationalityService;

    @Inject
    public NationalityHelperImpl(NationalityService nationalityService) {
        this.nationalityService = nationalityService;
    }

    @Override
    public Observable<Response<NationalityResponse>> getNationality(String name) {
        return nationalityService.getNationality(name);
    }
}
