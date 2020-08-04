package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;
import com.openclassrooms.realestatemanager.repositories.PropertyDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class PropertyViewModel extends ViewModel {

    private final AgentDataRepository agentDataSource;
    private final PropertyDataRepository propertyDataSource;
    private final Executor executor; // permits to realize asynchronous requests


    private LiveData<List<Property>> mPropertyList;


    public PropertyViewModel(AgentDataRepository agentDataSource, PropertyDataRepository propertyDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.propertyDataSource = propertyDataSource;
        this.executor = executor;
    }


    public void init(long agentId){
        if (mPropertyList == null){
            mPropertyList = propertyDataSource.getPropertyList();
        }
    }

    // ---------
    // AGENT
    // ---------


    public void createAgent(Agent agent){
        executor.execute(()->
                agentDataSource.createAgent(agent));
    }

    public LiveData<Agent> getAgent(long id){
        return agentDataSource.getAgent(id);
    }

    public LiveData<List<Agent>> getAgentList(){
        return agentDataSource.getAgentList();
    }

    public void updateAgent(Agent agent){
        executor.execute(()->
                agentDataSource.updateAgent(agent));
    }

    public void deleteAgent(Agent agent){
        executor.execute(()->
                agentDataSource.deleteAgent(agent));
    }

    // ---------
    // PROPERTY
    // ---------

    public void createProperty(Property property){
        executor.execute(()->
                propertyDataSource.createProperty(property));
    }

    public LiveData<Property> getProperty(long agentId){
        return propertyDataSource.getProperty(agentId);
    }

    public LiveData<List<Property>> getPropertyList(){
        return propertyDataSource.getPropertyList();
    }

    public void updateProperty(Property property){
        executor.execute(()->
                propertyDataSource.updateProperty(property));
    }

}
