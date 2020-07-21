package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.controllers.fragments.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.models.Property;

public class PropertiesViewHolder extends RecyclerView.ViewHolder{

    // FOR DATA
    private Context mContext;
    public static final String PROPERTY_CITY = "PROPERTY CITY";
    public static final String PROPERTY_PRICE = "PROPERTY PRICE";

    // FOR UI
    private TextView mPropertyPrice, mPropertyCity;
    private ImageView mPropertyPhoto;

    public PropertiesViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mPropertyPrice = itemView.findViewById(R.id.property_price);
        mPropertyCity = itemView.findViewById(R.id.property_city);
        mPropertyPhoto = itemView.findViewById(R.id.property_photo);
    }

    public void displayData(final Property property) {

        mPropertyCity.setText(property.getCity());
        mPropertyPrice.setText(String.valueOf(property.getPrice()));

        Glide.with(mContext)
                .load(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(mPropertyPhoto);

/*
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) mContext;

                PropertyDetailFragment fragment = new PropertyDetailFragment();

                // pass data to other fragment
                Bundle bundle = new Bundle();
                bundle.putString(PROPERTY_CITY, property.getCity());
                bundle.putInt(PROPERTY_PRICE, property.getPrice());
                fragment.setArguments(bundle);

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

 */
    }
}
