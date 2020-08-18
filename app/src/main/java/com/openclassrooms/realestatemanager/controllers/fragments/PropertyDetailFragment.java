package com.openclassrooms.realestatemanager.controllers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;


public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA
    private PropertyDetailFragmentViewModel mViewModel;

    // FOR UI
    private TextView mTxtViewCity, mTxtViewAddress, mTxtViewPrice, mTxtViewType, mTxtViewSurface, mTxtViewNbrOfRoom,
            mTxtViewNbrOfBedroom, mTxtViewNbrOfBathroom, mTxtViewDescription, mTxtViewAgentInCharge;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail,container,false);

        configureViewModel();
        configureViews(view);

        Bundle bundle = getArguments();

        if (bundle != null){
            long propertyId = bundle.getLong(PROPERTY_ID_KEY);
            long agentId = bundle.getLong(PROPERTY_AGENT_ID_KEY);

            LiveData<Property> propertyDetail = mViewModel.getProperty(agentId, propertyId);
            propertyDetail.observe(this, property ->{
                mTxtViewCity.setText(property.getCity());
                mTxtViewAddress.setText(property.getAddress());
                mTxtViewPrice.setText(property.getPrice());
                mTxtViewType.setText(property.getType());
                mTxtViewSurface.setText(property.getSurface());
                mTxtViewNbrOfRoom.setText(property.getNumberOfRooms());
                mTxtViewNbrOfBedroom.setText(property.getNumberOfBedrooms());
                mTxtViewNbrOfBathroom.setText(property.getNumberOfBathRooms());
                mTxtViewDescription.setText(property.getDescription());
                mTxtViewAgentInCharge.setText(property.getAgentNameSurname());

            });

        }

        return view;
    }

    private void configureViews(View view) {
        mTxtViewCity = view.findViewById(R.id.detail_fragment_property_city);
        mTxtViewAddress = view.findViewById(R.id.detail_fragment_property_address);
        mTxtViewPrice = view.findViewById(R.id.detail_fragment_property_price);
        mTxtViewType = view.findViewById(R.id.detail_fragment_property_type);
        mTxtViewSurface = view.findViewById(R.id.detail_fragment_property_surface);
        mTxtViewNbrOfRoom = view.findViewById(R.id.detail_fragment_property_number_of_room);
        mTxtViewNbrOfBedroom = view.findViewById(R.id.detail_fragment_property_number_of_bedroom);
        mTxtViewNbrOfBathroom = view.findViewById(R.id.detail_fragment_property_number_of_bathroom);
        mTxtViewDescription = view.findViewById(R.id.detail_fragment_property_description);
        mTxtViewAgentInCharge = view.findViewById(R.id.detail_fragment_property_agent);

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(PropertyDetailFragmentViewModel.class);
    }

}
