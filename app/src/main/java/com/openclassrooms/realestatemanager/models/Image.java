package com.openclassrooms.realestatemanager.models;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "image_table")
public class Image {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long propertyId;

    private String imagePath;

    private String imageTitle;

    @Ignore
    public Image(long id,long propertyId, String imagePath) {
        this.id = id;
        this.propertyId = propertyId;
        this.imagePath = imagePath;
    }

    public Image(long propertyId, String imagePath, String imageTitle){
        this.propertyId = propertyId;
        this.imagePath = imagePath;
        this.imageTitle = imageTitle;
    }

    public Image() {

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

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }


    public static Image fromContentValues(ContentValues values){
        final Image image = new Image();
        if (values.containsKey("id")) image.setId(values.getAsLong("id"));
        if (values.containsKey("propertyId")) image.setPropertyId(values.getAsLong("propertyId"));
        if (values.containsKey("imagePath")) image.setImagePath(values.getAsString("imagePath"));
        if (values.containsKey("imageTitle")) image.setImageTitle(values.getAsString("imageTitle"));
        return image;
    }
}