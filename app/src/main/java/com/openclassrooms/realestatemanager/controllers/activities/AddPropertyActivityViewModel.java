package com.openclassrooms.realestatemanager.controllers.activities;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.concurrent.Executor;

public class AddPropertyActivityViewModel extends ViewModel {

    private final AgentDataRepository agentDataRepository;
    private final PropertyDataRepository propertyDataRepository;
    private final ImageDataRepository imageDataRepository;
    private final Executor executor; // permits to realize asynchronous requests

    private MutableLiveData<String> mAgentSelected, mDateSelected;
    private MutableLiveData<Long> mAgentIdSelected;
    private MutableLiveData<LinearLayout> mLinearLayout;



    private MutableLiveData<ImageView> mImagePhotoAdded;

    public AddPropertyActivityViewModel(AgentDataRepository agentDataRepository, PropertyDataRepository propertyDataRepository, ImageDataRepository imageDataRepository, Executor executor) {
        this.agentDataRepository = agentDataRepository;
        this.propertyDataRepository = propertyDataRepository;
        this.imageDataRepository = imageDataRepository;
        this.executor = executor;
    }

    // ---------
    // PROPERTY
    // ---------

    public void createProperty(Property property){
        executor.execute(()->
                propertyDataRepository.createProperty(property));
    }


    // ---------
    // IMAGE
    // ---------

    public void createImage(Image image){
        executor.execute(() -> {
            imageDataRepository.createImage(image);
        });
    }


    // -------------------
    public MutableLiveData<String> getAgentSelected(){
        if (mAgentSelected == null) {
            mAgentSelected = new MutableLiveData<>();
            mAgentSelected.setValue("");
        }
        return mAgentSelected;
    }

    public MutableLiveData<String> getDateSelected(){
        if (mDateSelected == null) {
            mDateSelected = new MutableLiveData<>();
            mDateSelected.setValue("");
        }
        return mDateSelected;
    }

    public MutableLiveData<Long> getAgentIdSelected(){
        if (mAgentIdSelected == null){
            mAgentIdSelected = new MutableLiveData<>();
            mAgentIdSelected.setValue(0L);
        }
        return mAgentIdSelected;
    }

    public MutableLiveData<LinearLayout> getLinearLayout(){
        if (mLinearLayout == null){
            mLinearLayout = new MutableLiveData<>();
        }
        return mLinearLayout;
    }

    public MutableLiveData<ImageView> getImagePhotoAdded() {
        if(mImagePhotoAdded == null){
            mImagePhotoAdded = new MutableLiveData<>();
        }
        return mImagePhotoAdded;
    }

}
