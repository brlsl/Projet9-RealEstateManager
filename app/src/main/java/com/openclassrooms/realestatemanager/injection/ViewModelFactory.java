package com.openclassrooms.realestatemanager.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.REMViewModel;

import com.openclassrooms.realestatemanager.controllers.activities.BasePropertyActivityViewModel;
import com.openclassrooms.realestatemanager.controllers.fragments.DetailPropertyFragmentViewModel;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final ImageDataRepository imageDataSource;
    private final Executor executor;

    public ViewModelFactory(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource,
                            ImageDataRepository imageDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.imageDataSource = imageDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(REMViewModel.class)) {
            return (T) new REMViewModel(agentDataSource, propertyDataSource, executor);
        }
        if (modelClass.isAssignableFrom(BasePropertyActivityViewModel.class)){
            return (T) new BasePropertyActivityViewModel(agentDataSource,propertyDataSource, imageDataSource, executor);
        }

        if (modelClass.isAssignableFrom(DetailPropertyFragmentViewModel.class)){
            return (T) new DetailPropertyFragmentViewModel( propertyDataSource, imageDataSource);
        }

        throw  new IllegalArgumentException("Unknown ViewModel Class");
    }
}
