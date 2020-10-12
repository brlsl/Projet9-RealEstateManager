package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.models.api.Geocode;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleApiServices {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build();

    @GET("geocode/json?")
    Observable<Geocode> getGpsPosition(@Query("address") String address,
                                       @Query("key") String apiKey);
}
