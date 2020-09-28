package com.openclassrooms.realestatemanager;

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

public class REMViewModel extends ViewModel {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final ImageDataRepository imageDataSource;
    private final Executor executor; // permits to realize asynchronous requests


    private MutableLiveData<List<Property>> mFilteredPropertyList;
    private MutableLiveData<List<String>> mPointsOfInterestList;

    private MutableLiveData<Date> mDateAvailableMin, mDateAvailableMax, mDateSoldMin, mDateSoldMax;

    public REMViewModel(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource,
                        ImageDataRepository imageDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.imageDataSource = imageDataSource;
        this.executor = executor;
    }


    // ---------
    // AGENT
    // ---------


    public void createAgent(Agent agent){
        executor.execute(()->
                agentDataSource.createAgent(agent));
    }

    public LiveData<Agent> getAgent(long id){
        return agentDataSource.getAgent(id);
    }

    public LiveData<List<Agent>> getAgentList(){
        return agentDataSource.getAgentList();
    }

    public void updateAgent(Agent agent){
        executor.execute(()->
                agentDataSource.updateAgent(agent));
    }

    public void deleteAgent(Agent agent){
        executor.execute(()->
                agentDataSource.deleteAgent(agent));
    }

    // ---------
    // PROPERTY
    // ---------

    public void createProperty(Property property){
        executor.execute(()->
                propertyDataSource.createProperty(property));
    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDataSource.getProperty(propertyId, agentId);
    }

    public LiveData<List<Property>> getPropertyList(){
        return propertyDataSource.getPropertyList();
    }
/*
    public LiveData<List<Property>> filterPropertyListOneType(String type, int surfaceMin, int surfaceMax, int priceMin, int priceMax, int nbrRoomMin, int nbrRoomMax,
                                                              Date dateAvailableMin, Date dateAvailableMax, Date dateSoldMin, Date dateSoldMax){
        return propertyDataSource.filterPropertyListOneType(type, surfaceMin,surfaceMax,priceMin,priceMax, nbrRoomMin,nbrRoomMax,
                dateAvailableMin,dateAvailableMax,dateSoldMin,dateSoldMax);
    }
*/
    public LiveData<List<Property>> filterPropertyListAllType(int surfaceMin, int surfaceMax, int priceMin, int priceMax, int nbrRoomMin, int nbrRoomMax,
                                                              Date dateAvailableMin, Date dateAvailableMax, Date dateSoldMin, Date dateSoldMax, boolean isAvailable){
        return propertyDataSource.filterPropertyListAllType(surfaceMin,surfaceMax,priceMin,priceMax, nbrRoomMin,nbrRoomMax,
                dateAvailableMin,dateAvailableMax,dateSoldMin,dateSoldMax, isAvailable);
    }
    /*
    public void updateProperty(Property property){
        executor.execute(()->
                propertyDataSource.updateProperty(property));
    }

     */


    // ---------
    // IMAGE
    // --------

    public void createImage(Image image){
        executor.execute(() -> {
            imageDataSource.createImage(image);
        });
    }

    public LiveData<Image> getImage(long imageId, long propertyId){
        return imageDataSource.getImage(imageId, propertyId);
    }

    public LiveData<List<Image>> getImageListAllProperties(){
        return imageDataSource.getImageListAllProperties();
    }

    public LiveData<List<Image>> getImageListOneProperty(long propertyId){
        return imageDataSource.getImageListOneProperty(propertyId);
    }

    // Date

    public MutableLiveData<Date> getDateAvailableMin() {
        if (mDateAvailableMin == null){
            mDateAvailableMin = new MutableLiveData<>();
        }
        return mDateAvailableMin;
    }

    public MutableLiveData<Date> getDateAvailableMax() {
        if (mDateAvailableMax == null){
            mDateAvailableMax = new MutableLiveData<>();
        }
        return mDateAvailableMax;
    }


    public MutableLiveData<Date> getDateSoldMin() {
        if (mDateSoldMin == null){
            mDateSoldMin = new MutableLiveData<>();
        }
        return mDateSoldMin;
    }

    public MutableLiveData<Date> getDateSoldMax() {
        if (mDateSoldMax == null){
            mDateSoldMax = new MutableLiveData<>();
        }
        return mDateSoldMax;
    }

    // List

    public MutableLiveData<List<Property>> getFilteredPropertyList(){
        if (mFilteredPropertyList == null){
            mFilteredPropertyList = new MutableLiveData<>();
        }
        return mFilteredPropertyList;
    }

    public MutableLiveData<List<String>> getPointsOfInterestList() {
        if (mPointsOfInterestList == null){
            mPointsOfInterestList = new MutableLiveData<>();
        }
        return mPointsOfInterestList;
    }
}
