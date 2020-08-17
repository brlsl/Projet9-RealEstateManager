package com.openclassrooms.realestatemanager.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "image_table")
public class Image {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long propertyId;

    private String imagePath;

    @Ignore
    public Image(long id,long propertyId, String imagePath) {
        this.id = id;
        this.propertyId = propertyId;
        this.imagePath = imagePath;
    }

    public Image(long propertyId, String imagePath){
        this.propertyId = propertyId;
        this.imagePath = imagePath;

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}