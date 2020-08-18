package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class REMViewModel extends ViewModel {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final ImageDataRepository imageDataSource;
    private final Executor executor; // permits to realize asynchronous requests


    private LiveData<List<Property>> mPropertyList;


    public REMViewModel(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource,
                        ImageDataRepository imageDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.imageDataSource = imageDataSource;
        this.executor = executor;
    }

    public void init(long agentId){
        if (mPropertyList == null){
            mPropertyList = propertyDataSource.getPropertyList();
        }
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

    public void updateProperty(Property property){
        executor.execute(()->
                propertyDataSource.updateProperty(property));
    }


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

    public LiveData<List<Image>> getImageList(){
        return imageDataSource.getImageList();
    }

    public void updateImage(Image image){
        executor.execute(() ->
                imageDataSource.updateImage(image));
    }

    public void deleteImage(Image image){
        executor.execute(() ->
                imageDataSource.deleteImage(image));
    }
}
