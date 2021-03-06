package com.openclassrooms.realestatemanager.controllers.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;


import java.util.List;



public class DetailPropertyFragmentViewModel extends ViewModel {

    private PropertyDataRepository propertyDataSource;
    private ImageDataRepository imageDataSource;

    public DetailPropertyFragmentViewModel(PropertyDataRepository propertyDataSource,
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
