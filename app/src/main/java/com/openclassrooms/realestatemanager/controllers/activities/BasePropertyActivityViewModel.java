package com.openclassrooms.realestatemanager.controllers.activities;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class BasePropertyActivityViewModel extends ViewModel {

    private final AgentDataRepository agentDataRepository;
    private final PropertyDataRepository propertyDataRepository;
    private final ImageDataRepository imageDataRepository;
    private final Executor executor; // permits to realize asynchronous requests

    private MutableLiveData<Date> mDateAvailable;
    private MutableLiveData<Date> mDateSold;

    private MutableLiveData<List<Bitmap>> mBitmapList;

    private MutableLiveData<Agent> mAgent;

    private MutableLiveData<List<String>> mPathList, mPointsOfInterestList;

    private MutableLiveData<String> mAgentNameSurname;


    public BasePropertyActivityViewModel(AgentDataRepository agentDataRepository, PropertyDataRepository propertyDataRepository, ImageDataRepository imageDataRepository, Executor executor) {
        this.agentDataRepository = agentDataRepository;
        this.propertyDataRepository = propertyDataRepository;
        this.imageDataRepository = imageDataRepository;
        this.executor = executor;
    }



    // ------
    // AGENT
    // ------

    public LiveData<Agent> getAgent(long id){
        return agentDataRepository.getAgent(id);
    }

    // ---------
    // PROPERTY
    // ---------

    public void createProperty(Property property){
        executor.execute(()->
                propertyDataRepository.createProperty(property));
    }

    public LiveData<List<Property>> getPropertyList(){
        return propertyDataRepository.getPropertyList();
    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDataRepository.getProperty(propertyId, agentId);
    }

    public void updateProperty(long agentId, String city, String type, String address, String price, String surface,
                               String numberOfRooms, String numberOfBedrooms, String numberOfBathRooms, String description,
                               Date dateAvailable, Date dateSold, String agentNameSurname,
                               List<String> pointOfInterest, String imagePath, boolean isAvailable, long propertyId){
        executor.execute(()->
                propertyDataRepository.updateProperty(agentId, city, type, address, price, surface, numberOfRooms, numberOfBedrooms, numberOfBathRooms,
                        description, dateAvailable, dateSold, agentNameSurname, pointOfInterest,imagePath, isAvailable, propertyId));
    }

    // ---------
    // IMAGE
    // ---------

    public void createImage(Image image){
        executor.execute(() -> {
            imageDataRepository.createImage(image);
        });
    }

    public LiveData<List<Image>> getImageListOneProperty(long propertyId){
        return imageDataRepository.getImageListOneProperty(propertyId);
    }

    public void updateImage(String imagePath, long imageId){
        executor.execute(() ->
                imageDataRepository.updateImage(imagePath, imageId));
    }

    public void deleteImagesOneProperty(long propertyId){
        executor.execute(() ->
                imageDataRepository.deleteImagesOneProperty(propertyId));
    }

    // -------------------

    public MutableLiveData<Agent> getAgentMutableLiveData(){
        if (mAgent == null) {
            mAgent = new MutableLiveData<>();
        }
        return mAgent;
    }


    public MutableLiveData<Date> getDateAvailable(){
        if (mDateAvailable == null) {
            mDateAvailable = new MutableLiveData<>();
        }
        return mDateAvailable;
    }


    public MutableLiveData<List<Bitmap>> getBitmapList() {
        if (mBitmapList == null){
            mBitmapList = new MutableLiveData<>();
        }
        return mBitmapList;
    }

    public MutableLiveData<List<String>> getPathList() {
        if (mPathList == null){
            mPathList = new MutableLiveData<>();
        }

        return mPathList;
    }


    public MutableLiveData<String> getAgentNameSurname(){
        if (mAgentNameSurname == null) {
            mAgentNameSurname = new MutableLiveData<>();
        }
        return mAgentNameSurname;
    }

    public MutableLiveData<Date> getDateSold() {
        if (mDateSold == null) {
            mDateSold = new MutableLiveData<>();
        }
        return mDateSold;
    }

    public MutableLiveData<List<String>> getPointsOfInterestList() {
        if (mPointsOfInterestList == null){
            mPointsOfInterestList = new MutableLiveData<>();
        }
        return mPointsOfInterestList;
    }
}
