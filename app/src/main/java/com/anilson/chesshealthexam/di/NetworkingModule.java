package com.anilson.chesshealthexam.di;

import com.anilson.chesshealthexam.BuildConfig;
import com.anilson.chesshealthexam.networking.apis.GenderService;
import com.anilson.chesshealthexam.networking.apis.NationalityService;
import com.anilson.chesshealthexam.networking.apis.AgeService;

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
        return buildRetrofit("https://api.agify.io/", okHttpClient) //TODO constants
                .build()
                .create(AgeService.class);
    }

    @Provides
    @Singleton
    GenderService provideGenderService(OkHttpClient okHttpClient) {
        return buildRetrofit("https://api.genderize.io/", okHttpClient) //TODO constants
                .build()
                .create(GenderService.class);
    }

    @Provides
    @Singleton
    NationalityService provideNationalityService(OkHttpClient okHttpClient) {
        return buildRetrofit("https://api.nationalize.io/", okHttpClient) //TODO constants
                .build()
                .create(NationalityService.class);
    }

    private Retrofit.Builder buildRetrofit(String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient);
    }
}
