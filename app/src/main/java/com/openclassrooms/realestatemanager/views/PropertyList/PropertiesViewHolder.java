package com.openclassrooms.realestatemanager.views.PropertyList;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;

public class PropertiesViewHolder extends RecyclerView.ViewHolder{

    // FOR DATA
    private Context mContext;

    // FOR UI
    private TextView mPropertyPrice, mPropertyCity, mPropertyType;
    private ImageView mPropertyImageView;

    public PropertiesViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mPropertyPrice = itemView.findViewById(R.id.property_price);
        mPropertyCity = itemView.findViewById(R.id.property_city);
        mPropertyType = itemView.findViewById(R.id.property_type);
        mPropertyImageView = itemView.findViewById(R.id.properties_list_property_imageview);
    }

    public void displayData(final Property property) {
        mPropertyCity.setText(property.getCity());
        mPropertyPrice.setText(Utils.formatPrice(String.valueOf(property.getPrice())) + " €");
        mPropertyType.setText(property.getType());


        File file = new File(property.getMainImagePath());
        if (file.exists()) {
            Glide.with(mContext)
                    .load(file)
                    .centerCrop()
                    .into(mPropertyImageView);
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(mPropertyImageView);
        }
    }
}
