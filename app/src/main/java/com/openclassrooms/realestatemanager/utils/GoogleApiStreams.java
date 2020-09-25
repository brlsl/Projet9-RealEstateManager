package com.openclassrooms.realestatemanager.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.models.api.Geocode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class GoogleApiStreams {

    public static Observable<Geocode> streamFetchLocationFromAddress(String address, String apiKey){
        GoogleApiServices service = GoogleApiServices.retrofit.create(GoogleApiServices.class);
        return service
                .getGpsPosition(address, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


}
