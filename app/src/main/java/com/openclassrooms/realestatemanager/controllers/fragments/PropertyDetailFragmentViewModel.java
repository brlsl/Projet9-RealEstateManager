package com.openclassrooms.realestatemanager.controllers.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.api.Geocode;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;


import java.util.List;



public class PropertyDetailFragmentViewModel extends ViewModel {

    private PropertyDataRepository propertyDataSource;
    private ImageDataRepository imageDataSource;

    private MutableLiveData<Geocode> mResult;


    public PropertyDetailFragmentViewModel(PropertyDataRepository propertyDataSource,
                                           ImageDataRepository imageDataSource) {

        this.propertyDataSource = propertyDataSource;
        this.imageDataSource = imageDataSource;

    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDataSource.getProperty(propertyId, agentId);
    }

    public LiveData<List<Image>> getImageListOneProperty(long propertyId){
        return imageDataSource.getImageListOneProperty(propertyId);
    }

}
