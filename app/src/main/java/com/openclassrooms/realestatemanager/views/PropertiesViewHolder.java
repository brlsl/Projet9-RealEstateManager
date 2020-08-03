package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.models.Property;

public class PropertiesViewHolder extends RecyclerView.ViewHolder{

    // FOR DATA
    private Context mContext;

    // FOR UI
    private TextView mPropertyPrice, mPropertyCity, mPropertyType;
    private ImageView mPropertyPhoto;

    public PropertiesViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mPropertyPrice = itemView.findViewById(R.id.property_price);
        mPropertyCity = itemView.findViewById(R.id.property_city);
        mPropertyType = itemView.findViewById(R.id.property_type);
        mPropertyPhoto = itemView.findViewById(R.id.property_photo);
    }

    public void displayData(final Property property) {
        mPropertyCity.setText(property.getCity());
        mPropertyPrice.setText(String.valueOf(property.getPrice()));
        mPropertyType.setText(property.getType());


        Glide.with(mContext)
                .load(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(mPropertyPhoto);
    }
}
