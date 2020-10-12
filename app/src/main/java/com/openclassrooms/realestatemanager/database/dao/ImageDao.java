package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.models.Image;

import java.util.List;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM image_table WHERE propertyId =:propertyId")
    Cursor getImagesWithCursor(long propertyId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long createImage(Image image);

    @Query("SELECT * FROM image_table")
    LiveData<List<Image>> getImageListAllProperties();

    @Query("SELECT * FROM image_table WHERE propertyId = :propertyId")
    LiveData<List<Image>> getImageListOfOneProperty(long propertyId);

    @Query("SELECT * FROM image_table WHERE propertyId = :propertyId AND id = :imageId" )
    LiveData<Image> getImage(long imageId, long propertyId);

    @Update (entity = Image.class)
    int updateImage(Image image);

    @Query("DELETE FROM image_table WHERE propertyId =:propertyId")
    void deleteImagesOneProperty(long propertyId);

}