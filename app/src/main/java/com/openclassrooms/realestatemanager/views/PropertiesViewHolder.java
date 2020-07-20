package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

public class PropertiesViewHolder extends RecyclerView.ViewHolder {

    // FOR DATA
    private Context mContext;

    // for UI
    private TextView mPropertyPrice, mPropertyCity;
    private ImageView mPropertyPhoto;

    public PropertiesViewHolder(@NonNull View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mPropertyPrice = itemView.findViewById(R.id.property_price);
        mPropertyCity = itemView.findViewById(R.id.property_city);
        mPropertyPhoto = itemView.findViewById(R.id.property_photo);
    }

    public void displayData(Property property) {
        mPropertyPrice.setText(String.valueOf(property.getPrice()));
        mPropertyCity.setText(property.getCity());

        Glide.with(mContext)
                .load(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(mPropertyPhoto);
    }
}
