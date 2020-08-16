package com.openclassrooms.realestatemanager.controllers.activities;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.concurrent.Executor;

public class AddPropertyActivityViewModel extends ViewModel {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final Executor executor; // permits to realize asynchronous requests

    private MutableLiveData<String> mAgentSelected, mDateSelected;
    private MutableLiveData<Long> mAgentIdSelected;
    private MutableLiveData<LinearLayout> mLinearLayout;



    private MutableLiveData<ImageView> mImagePhotoAdded;

    public AddPropertyActivityViewModel(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }

    // ---------
    // PROPERTY
    // ---------

    public void createProperty(Property property){
        executor.execute(()->
                propertyDataSource.createProperty(property));
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
