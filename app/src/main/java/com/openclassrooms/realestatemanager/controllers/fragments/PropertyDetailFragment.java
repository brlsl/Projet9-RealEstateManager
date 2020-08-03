package com.openclassrooms.realestatemanager.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_OBJECT;


public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA

    // FOR UI
    TextView mPropertyCity, mPropertyPrice, mPropertyType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail,container,false);

        Bundle bundle = getArguments();

        if (bundle != null){

            Property property = bundle.getParcelable(PROPERTY_OBJECT);

            mPropertyCity = view.findViewById(R.id.detail_activity_property_city);
            mPropertyPrice = view.findViewById(R.id.detail_fragment_property_price);
            mPropertyType = view.findViewById(R.id.detail_activity_property_type);

            if (property != null) {
                mPropertyCity.setText(property.getCity());
                mPropertyPrice.setText(String.valueOf(property.getPrice()));
                mPropertyType.setText(property.getType());
            }

            System.out.println("Value of property id: " + property.getId() );
            System.out.println("Value of property agent id: " + property.getAgentId()
                    +property.getNumberOfBathRooms()
                    + property.getNumberOfRooms() );
        }

        return view;
    }

}
