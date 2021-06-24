package com.anilson.chesshealthexam.networking.helpers;

import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.models.GenderResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;

public class GenderHelperImpl implements GenderHelper {

    private final GenderService genderService;

    @Inject
    public GenderHelperImpl(GenderService genderService) {
        this.genderService = genderService;
    }

    @Override
    public Observable<Response<GenderResponse>> getGender(String name) {
        return genderService.getGender(name);
    }
}
