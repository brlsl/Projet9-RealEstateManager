package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static AgentDataRepository provideAgentDataSource(Context context){
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new AgentDataRepository(database.agentDao());
    }

    public static PropertyDataRepository providePropertyDataSource(Context context){
        RealEstateManagerDatabase database = RealEstateManagerDatabase.getInstance(context);
        return new PropertyDataRepository(database.propertyDao());
    }

    public static Executor provideExecutor(){return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context){
        AgentDataRepository dataSourceAgent = provideAgentDataSource(context);
        PropertyDataRepository dataSourceProperty = providePropertyDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceAgent,dataSourceProperty,executor);
    }
}
