package com.openclassrooms.realestatemanager.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_OBJECT;


public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA

    // FOR UI
    private TextView mCity, mAddress, mPrice, mType, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription, mAgentInCharge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail,container,false);

        Bundle bundle = getArguments();

        if (bundle != null){

            Property property = bundle.getParcelable(PROPERTY_OBJECT);

            mCity = view.findViewById(R.id.detail_activity_property_city);
            mAddress = view.findViewById(R.id.detail_fragment_property_address);
            mPrice = view.findViewById(R.id.detail_fragment_property_price);
            mType = view.findViewById(R.id.detail_fragment_property_type);
            mSurface = view.findViewById(R.id.detail_fragment_property_surface);
            mNbrOfRoom = view.findViewById(R.id.detail_fragment_property_number_of_room);
            mNbrOfBedroom = view.findViewById(R.id.detail_fragment_property_number_of_bedroom);
            mNbrOfBathroom = view.findViewById(R.id.detail_fragment_property_number_of_bathroom);
            mDescription = view.findViewById(R.id.detail_fragment_property_description);
            mAgentInCharge = view.findViewById(R.id.detail_fragment_property_agent);

            if (property != null) {
                mCity.setText(property.getCity());
                mAddress.setText(property.getAddress());
                mPrice.setText(property.getPrice());
                mType.setText(property.getType());
                mSurface.setText(property.getSurface());
                mNbrOfRoom.setText(property.getNumberOfRooms());
                mNbrOfBathroom.setText(property.getNumberOfBathRooms());
                mNbrOfBedroom.setText(property.getNumberOfBedrooms());
                mDescription.setText(property.getDescription());
                //mAgentInCharge.setText();
            }

        }

        return view;
    }

}
