package com.openclassrooms.realestatemanager.controllers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

public class MapCustomWindowAdapter implements GoogleMap.InfoWindowAdapter{


    private View mView;
    private Property mProperty;

    @SuppressLint("InflateParams")
    public MapCustomWindowAdapter(Context context, Property property){
        mView = LayoutInflater.from(context).inflate(R.layout.custom_information_window_map, null);
        mProperty = property;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        ImageView propertyImage = mView.findViewById(R.id.custom_information_map_property_photo);
        Bitmap bitmap = BitmapFactory.decodeFile(mProperty.getMainImagePath());
        propertyImage.setImageBitmap(bitmap);

        TextView status = mView.findViewById(R.id.custom_information_map_property_textView_status);
        if (mProperty.isAvailable()){
            status.setText(mView.getContext().getString(R.string.available));
        }
        else {
            status.setText(mView.getContext().getString(R.string.sold));
        }


        TextView address = mView.findViewById(R.id.custom_information_map_address_and_city);
        address.setText(mProperty.getAddress());

        TextView price = mView.findViewById(R.id.custom_information_map_property_price);
        price.setText(String.valueOf(mProperty.getPrice()));

        TextView type = mView.findViewById(R.id.custom_information_map_property_type);
        type.setText(mProperty.getType());

        TextView currency = mView.findViewById(R.id.custom_information_map_property_currency);
        currency.setText(mProperty.getCurrency());

        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
