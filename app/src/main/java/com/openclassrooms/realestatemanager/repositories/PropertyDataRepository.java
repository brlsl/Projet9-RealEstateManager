package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.Date;
import java.util.List;

public class PropertyDataRepository {
    private PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao){
        this.propertyDao = propertyDao;
    }

    public LiveData<Property> getProperty(long propertyId, long agentId){
        return propertyDao.getProperty(propertyId, agentId);
    }

    public LiveData<List<Property>> getPropertyList(){
        return propertyDao.getPropertyList();
    }

    public void createProperty(Property property){
        propertyDao.createProperty(property);
    }


    public void updateProperty(Property property){
        propertyDao.updateProperty(property);
    }

    public LiveData<List<Property>> filterPropertyListAllType(int surfaceMin, int surfaceMax, int priceMin, int priceMax, int nbrRoomMin, int nbrRoomMax,
                                                              Date dateAvailableMin, Date dateAvailableMax, Date dateSoldMin, Date dateSoldMax, boolean isAvailable,
                                                              int numberOfPicturesMin, int numberOfPicturesMax, String city){
        return propertyDao.filterPropertyListAllType(surfaceMin,surfaceMax,priceMin,priceMax, nbrRoomMin,nbrRoomMax,
                dateAvailableMin,dateAvailableMax,dateSoldMin,dateSoldMax, isAvailable, numberOfPicturesMin, numberOfPicturesMax, city);
    }

}
