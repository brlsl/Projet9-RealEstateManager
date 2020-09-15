package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.activities.EditPropertyActivity;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.views.ImageSlider.ImageSliderAdapter;

import java.util.List;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;


public class PropertyDetailFragment extends BaseFragment {

    // FOR DATA
    private PropertyDetailFragmentViewModel mViewModel;

    // FOR UI
    private TextView mTxtViewCity, mTxtViewAddress, mTxtViewPrice, mTxtViewType, mTxtViewSurface, mTxtViewNbrOfRoom,
            mTxtViewNbrOfBedroom, mTxtViewNbrOfBathroom, mTxtViewDescription, mTxtViewDateAvailable, mTxtViewDateSoldField,
            mTxtViewDateSold, mTxtViewAgentNameSurname;

    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabEditProperty;

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

            onClickEditProperty(propertyId,agentId);

            System.out.println("Valeur de  property ID = " + propertyId + "Valeur de agentId = " + agentId);

            LiveData<Property> propertyDetail = mViewModel.getProperty(propertyId, agentId);
            propertyDetail.observe(this, property ->{
                mTxtViewCity.setText(property.getCity());
                mTxtViewAddress.setText(property.getAddress());
                mTxtViewPrice.setText(Utils.formatPrice(property.getPrice()));
                mTxtViewType.setText(property.getType());
                mTxtViewSurface.setText(property.getSurface());
                mTxtViewNbrOfRoom.setText(property.getNumberOfRooms());
                mTxtViewNbrOfBedroom.setText(property.getNumberOfBedrooms());
                mTxtViewNbrOfBathroom.setText(property.getNumberOfBathRooms());
                mTxtViewDescription.setText(property.getDescription());
                mTxtViewAgentNameSurname.setText(property.getAgentNameSurname());
                mTxtViewDateAvailable.setText(Utils.formatDateToString(property.getDateAvailable()));

                if (property.isAvailable()) {
                    mTxtViewDateSoldField.setVisibility(View.GONE);
                    mTxtViewDateSold.setVisibility(View.GONE);
                }
                else if (!property.isAvailable()){
                    mTxtViewDateSold.setText(Utils.formatDateToString(property.getDateSold()));
                    mTxtViewDateSoldField.setVisibility(View.VISIBLE);
                    mTxtViewDateSold.setVisibility(View.VISIBLE);
                }

            });

            LiveData<List<Image>> imageList = mViewModel.getImageListOneProperty(propertyId);

            imageList.observe(this, images -> {
                mViewPager2.setAdapter(new ImageSliderAdapter(images, mViewPager2));
                Log.e("PropertyDetailFragment", "Image list: "+ images.size());

                // method for dots indicator in viewpager2
                new TabLayoutMediator(mTabLayout, mViewPager2, new TabLayoutMediator.TabConfigurationStrategy(){
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }).attach();

            });



        }

        return view;
    }

    // ------ METHODS CONFIGURATION ------

    private void configureViews(View view) {
        mViewPager2 = view.findViewById(R.id.detail_fragment_viewpager2_image_slider);
        mTabLayout = view.findViewById(R.id.detail_fragment_tab_layout);
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
        mTxtViewDateSoldField = view.findViewById(R.id.detail_fragment_properrty_sold_date_field);
        mTxtViewDateSold = view.findViewById(R.id.detail_fragment_property_sold_date);
        mTxtViewAgentNameSurname = view.findViewById(R.id.detail_fragment_property_agent_name_surname);
        mFabEditProperty = view.findViewById(R.id.detail_fragment_fab_edit_property);
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(PropertyDetailFragmentViewModel.class);
    }

    // ------ LISTENER ------
    private void onClickEditProperty(long propertyId, long agentId){
        mFabEditProperty.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), EditPropertyActivity.class);
            intent.putExtra(PROPERTY_ID_KEY, propertyId);
            intent.putExtra(PROPERTY_AGENT_ID_KEY, agentId);
            startActivity(intent);
        });
    }

}
