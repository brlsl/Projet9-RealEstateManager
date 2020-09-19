package com.openclassrooms.realestatemanager.database.dao;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createProperty(Property property);

    @Query("SELECT * FROM property_table WHERE agentId = :agentId AND id= :propertyId")
    LiveData<Property> getProperty(long propertyId, long agentId);

    @Query("SELECT * FROM property_table")
    LiveData<List<Property>> getPropertyList();
/*
    @Query("SELECT * FROM property_table WHERE id = :propertyId AND agentId = :agentId")
    LiveData<Property> updateProperty(long propertyId, long agentId);


 */

    @Query("UPDATE property_table SET price =:price, city =:city WHERE id =:propertyId")
    int updatePropertyTest(String city, String price, long propertyId);
/*
    @Query("UPDATE property_table SET agentId =:agentId, type =:type, city=:city, address = :address, price = :price, surface = :surface, numberOfRooms =:numberOfRooms, numberOfBedrooms = :numberOfBedrooms, numberOfBathRooms =:numberOfBathRooms, description=:description,dateAvailable =:dateAvailable, dateSold=:dateSold, agentNameSurname=:agentNameSurname, pointsOfInterest=:pointOfInterest, mainImagePath=:mainImagePath, isAvailable=:isAvailable WHERE id =:propertyId")
    int updateProperty(long agentId, String city, String type, String address, String price, String surface,
                       String numberOfRooms, String numberOfBedrooms, String numberOfBathRooms, String description,
                       Date dateAvailable, Date dateSold, String agentNameSurname,
                       List<String> pointOfInterest, String mainImagePath, boolean isAvailable, long propertyId);
*/
    @Update(entity = Property.class)
    void updateProperty(Property property);

    // ------ DATABASE PROPERTY SEARCH ------

    // Queries for testing
    @Query("SELECT * FROM property_table WHERE (type =:type )")
    LiveData<List<Property>> searchPropertyTestString(String type);

    // Query for testing
    @Query("SELECT * FROM property_table WHERE (surface BETWEEN :surfaceMin AND :surfaceMax) " +
            "AND (price BETWEEN :priceMin AND :priceMax) AND pointsOfInterest")
    LiveData<List<Property>> searchPropertyTestInt(int surfaceMin,
                                                   int surfaceMax, int priceMin, int priceMax);
    // Query for testing
    @Query("SELECT * FROM property_table WHERE (dateAvailable >= :dateMin AND dateAvailable <= :dateMax) ")
    LiveData<List<Property>> searchPropertyTestDate(Date dateMin, Date dateMax);

    // QUERY FOR APP

    @Query("SELECT * FROM property_table WHERE (surface BETWEEN :surfaceMin AND :surfaceMax) AND" +
            " (price BETWEEN :priceMin AND :priceMax) AND (numberOfRooms BETWEEN :nbrRoomMin AND :nbrRoomMax) AND " +
            "(type IN (:type)) AND (dateAvailable >= :dateAvailableMin AND dateAvailable <= :dateAvailableMax )" +
            "AND (dateSold >= :dateSoldMin AND dateSold <= :dateSoldMax)")
    LiveData<List<Property>> searchProperty(int surfaceMin, int surfaceMax,
                                            int priceMin, int priceMax,
                                            int nbrRoomMin, int nbrRoomMax, String type,
                                            Date dateAvailableMin, Date dateAvailableMax,
                                            Date dateSoldMin, Date dateSoldMax);

}
