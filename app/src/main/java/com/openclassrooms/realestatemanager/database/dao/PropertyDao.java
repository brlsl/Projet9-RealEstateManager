package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM property_table")
    LiveData<List<Property>> getPropertyList();

    @Insert
    void createProperty(Property property);

    @Query("SELECT * FROM property_table WHERE agentId = :agentId")
    LiveData<Property> getProperty(long agentId);

    @Update
    void updateProperty(Property property);

}
