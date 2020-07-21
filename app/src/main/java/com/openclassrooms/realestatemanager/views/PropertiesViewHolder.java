package com.openclassrooms.realestatemanager.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.PropertyDetailActivity;
import com.openclassrooms.realestatemanager.models.Property;

public class PropertiesViewHolder extends RecyclerView.ViewHolder {

    // FOR DATA
    private Context mContext;
    public static final String PROPERTY_CITY = "property_city";
    public static final String PROPERTY_PRICE = "property_price";

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

    public void displayData(final Property property) {

        mPropertyCity.setText(property.getCity());
        mPropertyPrice.setText(String.valueOf(property.getPrice()));

        Glide.with(mContext)
                .load(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(mPropertyPhoto);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PropertyDetailActivity.class);
                intent.putExtra(PROPERTY_CITY, property.getCity());
                intent.putExtra(PROPERTY_PRICE, property.getPrice());
                view.getContext().startActivity(intent);
            }
        });
    }

}
