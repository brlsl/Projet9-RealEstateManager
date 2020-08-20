package com.openclassrooms.realestatemanager.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.ImageDao;
import com.openclassrooms.realestatemanager.models.Image;

import java.util.List;

public class ImageDataRepository {
    private ImageDao imageDao;

    public ImageDataRepository(ImageDao imageDao){
        this.imageDao = imageDao;
    }

    public LiveData<List<Image>> getImageListAllProperties(){
        return imageDao.getImageListAllProperties();
    }

    public LiveData<List<Image>> getImageListOneProperty(long propertyId){
        return imageDao.getImageListOfOneProperty(propertyId);
    }

    public LiveData<Image> getImage(long imageId, long propertyId){
        return imageDao.getImage(imageId, propertyId);
    }

    public void createImage(Image image){
        imageDao.createImage(image);
    }

    public void updateImage(Image image){
        imageDao.updateImage(image);
    }

    public void deleteImage(Image image){
        imageDao.deleteImage(image);
    }
}