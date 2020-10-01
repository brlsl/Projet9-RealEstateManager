package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.activities.EditPropertyActivity;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.api.Geocode;
import com.openclassrooms.realestatemanager.utils.GoogleApiStreams;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.views.ImageSlider.ImageSliderAdapter;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;


public class DetailPropertyFragment extends BaseFragment {

    // FOR DATA
    private DetailPropertyFragmentViewModel mViewModel;
    private static final String TAG = "PropertyDetailFragment";
    private String mPropertyLatLng;
    private static String API_KEY;

    // FOR UI
    private TextView mTxtViewCity, mTxtViewAddress, mTxtViewPrice, mTxtViewType, mTxtViewSurface, mTxtViewNbrOfRoom,
            mTxtViewNbrOfBedroom, mTxtViewNbrOfBathroom, mTxtViewDescription, mTxtViewDateAvailable, mTxtViewDateSoldField,
            mTxtViewDateSold, mTxtViewAgentNameSurname, mTxtViewPOI, mTxtViewStatus;
    private ViewPager2 mViewPager2;
    private TabLayout mTabLayout;
    private FloatingActionButton mFabEditProperty;
    private Disposable mDisposable;
    private ImageView mStaticMapImageView;
    private TextView mTxtViewNoConnexion;
    // ------ LIFECYCLE ------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        API_KEY = requireActivity().getString(R.string.api_key);

        View view = inflater.inflate(R.layout.fragment_property_detail,container,false);

        configureViewModel();
        configureViews(view);
        Bundle bundle = getArguments();

        if (bundle != null){
            long propertyId = bundle.getLong(PROPERTY_ID_KEY);
            long agentId = bundle.getLong(PROPERTY_AGENT_ID_KEY);

            onClickEditProperty(propertyId,agentId);

            System.out.println("Property ID value = " + propertyId + "AgentId value = " + agentId);

            LiveData<Property> propertyDetail = mViewModel.getProperty(propertyId, agentId);
            propertyDetail.observe(this, property ->{
                mTxtViewCity.setText(property.getCity());
                mTxtViewAddress.setText(property.getAddress());
                mTxtViewPrice.setText(Utils.formatPrice(String.valueOf(property.getPrice())));
                mTxtViewType.setText(String.valueOf(property.getType()));
                mTxtViewSurface.setText(String.valueOf(property.getSurface()));
                mTxtViewNbrOfRoom.setText(String.valueOf(property.getNumberOfRooms()));
                mTxtViewNbrOfBedroom.setText(String.valueOf(property.getNumberOfBedrooms()));
                mTxtViewNbrOfBathroom.setText(String.valueOf(property.getNumberOfBathRooms()));
                mTxtViewDescription.setText(property.getDescription());
                mTxtViewAgentNameSurname.setText(property.getAgentNameSurname());
                mTxtViewDateAvailable.setText(Utils.formatDateToString(property.getDateAvailable()));
                String status = property.isAvailable()? "Available": "Sold";
                mTxtViewStatus.setText(status);

                if (property.getPointsOfInterest().size()>0) {
                    String joinList = TextUtils.join(", ", property.getPointsOfInterest());
                    mTxtViewPOI.setText(joinList);
                } else
                    mTxtViewPOI.setText(R.string.No_points_of_interests_around);


                Log.d(TAG, "PROPERTY ADDRESS" + property.getCity());

                this.mDisposable = GoogleApiStreams.streamFetchLocationFromAddress(property.getAddress()+" " + property.getCity(), API_KEY)
                        .subscribeWith(new DisposableObserver<Geocode>() {
                            @Override
                            public void onNext(Geocode geocode) {
                                if (geocode.getResults().size()> 0){
                                    String latitude = geocode.getResults().get(0).getGeometry().getLocation().getLat().toString();
                                    String longitude = geocode.getResults().get(0).getGeometry().getLocation().getLng().toString();

                                    mPropertyLatLng = latitude+","+longitude;
                                    Log.d(TAG, "Property latitude" + geocode.getResults().get(0).getGeometry().getLocation().getLat().toString());
                                    Log.d(TAG, "property lng" + geocode.getResults().get(0).getGeometry().getLocation().getLng().toString());
                                }
                                else
                                    Log.d(TAG,"NO RESULT");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                String staticMapUrl1 = "https://maps.googleapis.com/maps/api/staticmap?markers=";
                                String staticMapUrl2 =  "&zoom=18&size=1024x800&key=";

                                if (mPropertyLatLng != null){
                                    mTxtViewNoConnexion.setVisibility(View.GONE);
                                    mStaticMapImageView.setVisibility(View.VISIBLE);
                                    Glide.with(requireContext())
                                            .load(staticMapUrl1 + mPropertyLatLng + staticMapUrl2 + API_KEY)
                                            .into(mStaticMapImageView);

                                }
                                else {
                                    mTxtViewNoConnexion.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_map_24_black,0,0);
                                    mTxtViewNoConnexion.setText("Map not available, please check property address");
                                }
                                Log.d(TAG, "mPropertyLatLng : " + mPropertyLatLng);
                                Log.d(TAG, "url link :" + staticMapUrl1+mPropertyLatLng+staticMapUrl2+API_KEY);
                            }
                        });


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
                mViewPager2.setAdapter(new ImageSliderAdapter(images));
                Log.d(TAG, "Image list: "+ images.size());

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
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
        mTxtViewDateSoldField = view.findViewById(R.id.detail_fragment_property_sold_date_field);
        mTxtViewDateSold = view.findViewById(R.id.detail_fragment_property_sold_date);
        mTxtViewAgentNameSurname = view.findViewById(R.id.detail_fragment_property_agent_name_surname);
        mFabEditProperty = view.findViewById(R.id.detail_fragment_fab_edit_property);
        mTxtViewNoConnexion = view.findViewById(R.id.textView_no_connexion_detail_property);
        mTxtViewPOI = view.findViewById(R.id.textView_point_of_interests_detail_property);
        mTxtViewStatus = view.findViewById(R.id.fragment_property_detail_status);

        mStaticMapImageView = view.findViewById(R.id.static_map_imageView_detail_property_activity);

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailPropertyFragmentViewModel.class);
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
