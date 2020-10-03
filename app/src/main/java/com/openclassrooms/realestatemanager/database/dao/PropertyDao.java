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
/*
    @Query("SELECT * FROM property_table WHERE id = :propertyId AND agentId = :agentId")
    LiveData<Property> updateProperty(long propertyId, long agentId);


 */

    @Update(entity = Property.class)
    int updateProperty(Property property);

    //@Query("UPDATE property_table SET  ")
    //int updateTest(Property property);

    // ------ DATABASE PROPERTY SEARCH ------
/*
    //(type =:input ) OR (LENGHT(:input) > 2)
    // WHERE (type =:input) OR (LENGTH(type) >=1)
    // Queries for testing
    @Query("SELECT * FROM property_table type " +
            "CASE " +
            "WHEN (LENGTH(:input) > 1) THEN (type)"+
            "END FROM property_table")
    LiveData<List<Property>> searchPropertyTestString(String input);

 */

    // test for String query
    @Query("SELECT * FROM property_table WHERE (type LIKE :input) ")
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
            "AND (isAvailable = :isAvailable)")
    LiveData<List<Property>> filterPropertyListAllType(int surfaceMin, int surfaceMax,
                                                       int priceMin, int priceMax,
                                                       int nbrRoomMin, int nbrRoomMax,
                                                       Date dateAvailableMin, Date dateAvailableMax,
                                                       Date dateSoldMin, Date dateSoldMax,
                                                       boolean isAvailable);

    /*
    // useless
    @Query("SELECT * FROM property_table WHERE (type LIKE :type) AND (surface BETWEEN :surfaceMin AND :surfaceMax) AND" +
            " (price BETWEEN :priceMin AND :priceMax) AND (numberOfRooms BETWEEN :nbrRoomMin AND :nbrRoomMax)" +
            "AND (dateAvailable >= :dateAvailableMin AND dateAvailable <= :dateAvailableMax )" +
            "AND (dateSold >= :dateSoldMin AND dateSold <= :dateSoldMax)")
    LiveData<List<Property>> filterPropertyListOneType(String type,
                                                       int surfaceMin, int surfaceMax,
                                                       int priceMin, int priceMax,
                                                       int nbrRoomMin, int nbrRoomMax,
                                                       Date dateAvailableMin, Date dateAvailableMax,
                                                       Date dateSoldMin, Date dateSoldMax);


     */

}
