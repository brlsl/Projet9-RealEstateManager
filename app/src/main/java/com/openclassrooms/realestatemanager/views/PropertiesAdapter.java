package com.openclassrooms.realestatemanager.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesViewHolder> {

    private List<Property> mPropertiesList;

    public PropertiesAdapter(List<Property> propertiesList) {
        mPropertiesList = propertiesList;
    }

    @NonNull
    @Override
    public PropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_property, parent,false);
        return new PropertiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertiesViewHolder holder, int position) {
        holder.displayData(mPropertiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPropertiesList.size();
    }


    public void updatePropertyList(List<Property> propertyList){
        mPropertiesList = propertyList;
        notifyDataSetChanged();
    }
}
