package com.openclassrooms.realestatemanager.controllers.fragments;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.repositories.AgentDataRepository;

import java.util.concurrent.Executor;

public class AddAgentDialogFragmentViewModel extends ViewModel {

    private AgentDataRepository agentDataRepository;
    private Executor executor;

    public AddAgentDialogFragmentViewModel(AgentDataRepository agentDataSource, Executor executor) {
        this.agentDataRepository = agentDataSource;
        this.executor = executor;
    }

    public void createAgent(Agent agent){
        executor.execute(()->
                agentDataRepository.createAgent(agent));
    }
}
