package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PropertyDao;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertyDataRepository {
    private PropertyDao propertyDao;

    public PropertyDataRepository(PropertyDao propertyDao){
        this.propertyDao = propertyDao;
    }

    public LiveData<Property> getProperty(long agentId){
        return propertyDao.getProperty(agentId);
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

}
