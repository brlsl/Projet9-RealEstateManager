package com.openclassrooms.realestatemanager.controllers.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyDetailFragmentViewModel extends ViewModel {
    private AgentDataRepository agentDataSource;
    private PropertyDataRepository propertyDataSource;
    private ImageDataRepository imageDataSource;
    private Executor executor;

    private MutableLiveData<String> city, address, price, type, surface, nbrOfRoom, nbrOfBedroom, nbrOfBathroom, description, agentInCharge;

    public PropertyDetailFragmentViewModel(AgentDataRepository agentDataSource,
                                           PropertyDataRepository propertyDataSource,
                                           ImageDataRepository imageDataSource, Executor executor) {

        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.imageDataSource = imageDataSource;
        this.executor = executor;
    }

    public LiveData<Agent> getAgent(long id){
        return agentDataSource.getAgent(id);
    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDataSource.getProperty(propertyId, agentId);
    }

    public LiveData<List<Image>> getImageList(){
        return imageDataSource.getImageList();
    }

}
