package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Property;

import java.util.Date;
import java.util.List;

@Dao
public interface PropertyDao {

    @Query("SELECT * FROM property_table WHERE agentId = :agentId")
    Cursor getPropertyWithCursor(long agentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createProperty(Property property);

    @Query("SELECT * FROM property_table WHERE agentId = :agentId AND id= :propertyId")
    LiveData<Property> getProperty(long propertyId, long agentId);

    @Query("SELECT * FROM property_table")
    LiveData<List<Property>> getPropertyList();

    @Update(entity = Property.class)
    int updateProperty(Property property);

    // test for String query
    @Query("SELECT * FROM property_table WHERE (type LIKE :input) OR LENGTH(:input) = 0 ")
    LiveData<List<Property>> searchPropertyTestString(String input);

    // Query for testing
    @Query("SELECT * FROM property_table WHERE (surface BETWEEN :surfaceMin AND :surfaceMax) " +
            "AND (price BETWEEN :priceMin AND :priceMax)")
    LiveData<List<Property>> searchPropertyTestInt(int surfaceMin,
                                                   int surfaceMax, int priceMin, int priceMax);
    // Query for testing
    @Query("SELECT * FROM property_table WHERE (dateAvailable >= :dateAvailableMin AND dateAvailable <= :dateAvailableMax) AND " +
            "(dateSold >= :dateSoldMin AND dateSold <= :dateSoldMax)")
    LiveData<List<Property>> searchPropertyTestDate(Date dateAvailableMin, Date dateAvailableMax, Date dateSoldMin, Date dateSoldMax);

    // Query for testing
    @Query("SELECT * FROM property_table WHERE (isAvailable = :isAvailable)")
    LiveData<List<Property>> searchPropertyBoolean(boolean isAvailable);


    // QUERY FOR APP
    @Query("SELECT * FROM property_table WHERE (surface BETWEEN :surfaceMin AND :surfaceMax) AND" +
            " (price BETWEEN :priceMin AND :priceMax) " +
            "AND (numberOfRooms BETWEEN :nbrRoomMin AND :nbrRoomMax)" +
            "AND (dateAvailable >= :dateAvailableMin AND dateAvailable <= :dateAvailableMax )" +
            "AND (dateSold >= :dateSoldMin AND dateSold <= :dateSoldMax) " +
            "AND (isAvailable = :isAvailable) " +
            "AND (numberOfPictures BETWEEN :numberOfPicturesMin AND :numberOfPicturesMax) " +
            "AND ((city LIKE :city) OR LENGTH(:city) = 0)")
    LiveData<List<Property>> filterPropertyListAllType(int surfaceMin, int surfaceMax,
                                                       int priceMin, int priceMax,
                                                       int nbrRoomMin, int nbrRoomMax,
                                                       Date dateAvailableMin, Date dateAvailableMax,
                                                       Date dateSoldMin, Date dateSoldMax,
                                                       boolean isAvailable,
                                                       int numberOfPicturesMin, int numberOfPicturesMax,
                                                       String city);
}
