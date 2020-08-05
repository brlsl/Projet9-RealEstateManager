package com.openclassrooms.realestatemanager.views.AgentList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Agent;

import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentViewHolder> {


    List<Agent> mAgentList;

    public AgentAdapter(List<Agent> agentList){
        mAgentList = agentList;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_agent_item_bottom_sheet_fragment, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        holder.display(mAgentList.get(position));
    }

    @Override
    public int getItemCount() {
        return mAgentList.size();
    }

    public void updateAgentList(List<Agent> agentList){
        mAgentList = agentList;
        notifyDataSetChanged();
    }

}
