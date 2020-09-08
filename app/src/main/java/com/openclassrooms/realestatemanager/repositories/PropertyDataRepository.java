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

    public void updateProperty(long agentId, String city, String type, String address, String price, String surface,
                               String numberOfRooms, String numberOfBedrooms, String numberOfBathRooms, String description,
                               Date dateAvailable, Date dateSold, String agentNameSurname,
                               List<String> pointOfInterest, String imagePath, boolean isAvailable, long propertyId){
        propertyDao.updateProperty(agentId, city, type, address, price, surface, numberOfRooms, numberOfBedrooms, numberOfBathRooms,
                description, dateAvailable, dateSold, agentNameSurname, pointOfInterest, imagePath, isAvailable, propertyId);
    }

}
