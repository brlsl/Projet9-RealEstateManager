package com.openclassrooms.realestatemanager.controllers.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;


public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA
    private PropertyDetailFragmentViewModel mViewModel;

    // FOR UI
    private TextView mTxtViewCity, mTxtViewAddress, mTxtViewPrice, mTxtViewType, mTxtViewSurface, mTxtViewNbrOfRoom,
            mTxtViewNbrOfBedroom, mTxtViewNbrOfBathroom, mTxtViewDescription, mTxtViewDateAvailable, mTxtViewAgentNameSurname;

    private ViewFlipper mViewFlipper;

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
                mTxtViewAgentNameSurname.setText(property.getAgentNameSurname());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                mTxtViewDateAvailable.setText(sdf.format(property.getDateAvailable()));

            });


            LiveData<List<Image>> imageList = mViewModel.getImageListOneProperty(propertyId);

            imageList.observe(this, images -> {
                for (int i = 0; i <images.size() ; i++) {
                    File imgFile = new File(images.get(i).getImagePath());

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        ImageView myImage = new ImageView(requireContext());

                        myImage.setImageBitmap(myBitmap);

                        mViewFlipper.addView(myImage);
                    }
                }
            });


        }



        return view;
    }

    private void configureViews(View view) {
        mViewFlipper = view.findViewById(R.id.detail_fragment_viewFlipper);

        mTxtViewCity = view.findViewById(R.id.detail_fragment_property_city);
        mTxtViewAddress = view.findViewById(R.id.detail_fragment_property_address);
        mTxtViewPrice = view.findViewById(R.id.detail_fragment_property_price);
        mTxtViewType = view.findViewById(R.id.detail_fragment_property_type);
        mTxtViewSurface = view.findViewById(R.id.detail_fragment_property_surface);
        mTxtViewNbrOfRoom = view.findViewById(R.id.detail_fragment_property_number_of_room);
        mTxtViewNbrOfBedroom = view.findViewById(R.id.detail_fragment_property_number_of_bedroom);
        mTxtViewNbrOfBathroom = view.findViewById(R.id.detail_fragment_property_number_of_bathroom);
        mTxtViewDescription = view.findViewById(R.id.detail_fragment_property_description);
        mTxtViewDateAvailable = view.findViewById(R.id.detail_fragment_property_date_available);
        mTxtViewAgentNameSurname = view.findViewById(R.id.detail_fragment_property_agent_name_surname);

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(PropertyDetailFragmentViewModel.class);
    }

}
