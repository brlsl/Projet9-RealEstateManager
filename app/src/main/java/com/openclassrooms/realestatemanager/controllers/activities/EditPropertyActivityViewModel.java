package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class EditPropertyActivityViewModel extends ViewModel {
    private final PropertyDataRepository propertyDataRepository;
    private final ImageDataRepository imageDataRepository;
    private final Executor executor; // permits to realize asynchronous requests

    private MutableLiveData<String> mPrice;

    public EditPropertyActivityViewModel(PropertyDataRepository propertyDataRepository, ImageDataRepository imageDataRepository, Executor executor) {

        this.propertyDataRepository = propertyDataRepository;
        this.imageDataRepository = imageDataRepository;
        this.executor = executor;
    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDataRepository.getProperty(propertyId, agentId);
    }

    public void updateProperty(Property property){
        executor.execute(()->
                propertyDataRepository.updateProperty(property));
    }

    public void createImage(Image image){
        executor.execute(() -> {
            imageDataRepository.createImage(image);
        });
    }

    public LiveData<List<Image>> getImageListOneProperty(long propertyId){
        return imageDataRepository.getImageListOneProperty(propertyId);
    }

    public void updateImage(Image image){
        executor.execute(() ->
                imageDataRepository.updateImage(image));
    }

    public MutableLiveData<String> getPrice() {
        if (mPrice == null){
            mPrice = new MutableLiveData<>();
        }
        return mPrice;
    }

}
