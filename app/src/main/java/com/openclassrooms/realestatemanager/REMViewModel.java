package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class REMViewModel extends ViewModel {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final Executor executor; // permits to realize asynchronous requests
    private MutableLiveData<List<Property>> mFilteredPropertyList;
    private MutableLiveData<List<String>> mPointsOfInterestList;
    private MutableLiveData<Date> mDateAvailableMin, mDateAvailableMax, mDateSoldMin, mDateSoldMax;

    public REMViewModel(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }

    // ---------
    // AGENT
    // ---------


    public void createAgent(Agent agent){
        executor.execute(()->
                agentDataSource.createAgent(agent));
    }

    public LiveData<List<Agent>> getAgentList(){
        return agentDataSource.getAgentList();
    }

    // ---------
    // PROPERTY
    // ---------


    public LiveData<List<Property>> getPropertyList(){
        return propertyDataSource.getPropertyList();
    }

    public LiveData<List<Property>> filterPropertyListAllType(int surfaceMin, int surfaceMax, int priceMin, int priceMax, int nbrRoomMin, int nbrRoomMax,
                                                              Date dateAvailableMin, Date dateAvailableMax, Date dateSoldMin, Date dateSoldMax, boolean isAvailable,
                                                              int numberOfPicturesMin, int numberOfPicturesMax, String city){
        return propertyDataSource.filterPropertyListAllType(surfaceMin,surfaceMax,priceMin,priceMax, nbrRoomMin,nbrRoomMax,
                dateAvailableMin,dateAvailableMax,dateSoldMin,dateSoldMax, isAvailable, numberOfPicturesMin, numberOfPicturesMax, city);
    }

    public void updateProperty(Property property){
        executor.execute(()->
                propertyDataSource.updateProperty(property));
    }

    // ------
    // DATE
    // ------

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

    // -------
    // LIST
    // -------

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
