package com.openclassrooms.realestatemanager.views.AgentList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Agent;

public class AgentViewHolder extends RecyclerView.ViewHolder {


    // FOR UI
    private ImageView mAgentPhoto;
    private TextView mAgentName, mAgentSurname;

    public AgentViewHolder(@NonNull View itemView) {
        super(itemView);
        mAgentName = itemView.findViewById(R.id.agent_name_rv_bottom_sheet);
        mAgentSurname = itemView.findViewById(R.id.agent_surname_rv_bottom_sheet);
    }

    public void display(Agent agent){
        mAgentName.setText(agent.getName());
        mAgentSurname.setText(agent.getSurname());
    }
}
