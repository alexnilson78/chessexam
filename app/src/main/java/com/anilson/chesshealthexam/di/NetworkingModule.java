package com.anilson.chesshealthexam.di;

import com.anilson.chesshealthexam.BuildConfig;
import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.helpers.AgeHelper;
import com.anilson.chesshealthexam.networking.helpers.AgeHelperImpl;
import com.anilson.chesshealthexam.networking.apis.AgeService;
import com.anilson.chesshealthexam.networking.helpers.GenderHelper;
import com.anilson.chesshealthexam.networking.helpers.NationalityHelper;
import com.anilson.chesshealthexam.repositories.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkingModule {

    @Provides
    @Singleton
    public OkHttpClient providesOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        return builder.build();
    }

    @Provides
    @Singleton
    AgeService provideAgeService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl("https://api.agify.io/") //TODO constants
                .client(okHttpClient)
                .build()
                .create(AgeService.class);
    }

    @Provides
    @Singleton
    AgeHelper provideAgeHelper(AgeHelperImpl ageHelper) {
        return ageHelper;
    }

    @Provides
    @Singleton
    GenderService provideGenderService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.genderize.io/") //TODO constants
                .client(okHttpClient)
                .build()
                .create(GenderService.class);
    }

    @Provides
    @Singleton
    GenderHelper provideGenderHelper(GenderHelper genderHelper) {
        return genderHelper;
    }

    @Provides
    @Singleton
    NationalityService provideNationalityService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.nationalize.io/") //TODO constants
                .client(okHttpClient)
                .build()
                .create(NationalityService.class);
    }

    @Provides
    @Singleton
    NationalityHelper provideNationalityHelper(NationalityHelper nationalityHelper) {
        return nationalityHelper;
    }
}
