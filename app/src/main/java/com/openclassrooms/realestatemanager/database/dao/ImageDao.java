package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void createImage(Image image);

    @Query("SELECT * FROM image_table")
    LiveData<List<Image>> getImageList();

    @Query("SELECT * FROM image_table WHERE propertyId = :propertyId")
    LiveData<Image> getImage(long propertyId);

    @Update
    void updateImage(Image image);

    @Delete
    void deleteImage(Image image);

}