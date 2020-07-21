package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;

public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA
    public static final String PROPERTY_CITY = "PROPERTY CITY";
    public static final String PROPERTY_PRICE = "PROPERTY PRICE";

    // FOR UI
    TextView mPropertyCity, mPropertyPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_property_detail,container,false);

        Bundle bundle = getArguments();

        if (bundle != null){
            String propertyCity = bundle.getString(PROPERTY_CITY, null);
            int propertyPrice = bundle.getInt(PROPERTY_PRICE,0);

            mPropertyCity = view.findViewById(R.id.detail_activity_property_city);
            mPropertyPrice = view.findViewById(R.id.detail_fragment_property_price);

            mPropertyCity.setText(propertyCity);
            mPropertyPrice.setText(String.valueOf(propertyPrice));
        }

        return view;
    }

}
