package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.models.api.Geocode;
import com.openclassrooms.realestatemanager.utils.GoogleApiStreams;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    // FOR UI
    private GoogleMap mMap;
    private Marker mMarker;

    // FOR DATA
    private static final String TAG =  "MAP ACTIVITY";
    private static final int REQUEST_CODE_EDIT_PROPERTY = 999;
    public static final String PROPERTY_KEY_MAP_ACTIVITY = "PROPERTY_KEY_MAP_ACTIVITY";
    private REMViewModel mViewModel;
    private Disposable mDisposable;
    private static String API_KEY;
    private HashMap<LatLng, Property> mHashMap = new HashMap<>();


    // ------ LIFE CYCLE ------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configure map
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        API_KEY = getString(R.string.api_key);
        configureViewModel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mDisposable != null && !this.mDisposable.isDisposed())
            this.mDisposable.dispose();
    }


    // ------ MAP SETTINGS ------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mViewModel.getPropertyList().observe(this, propertyList -> {
            for (int i = 0; i < propertyList.size() ; i++) {
                int finalI = i;
                this.mDisposable = GoogleApiStreams.streamFetchLocationFromAddress(propertyList.get(i).getAddress()+" " +
                        propertyList.get(i).getCity(), API_KEY)
                        .subscribeWith(new DisposableObserver<Geocode>() {
                            @Override
                            public void onNext(Geocode geocode) {
                                if (geocode.getResults().size()> 0){
                                    double latitude = geocode.getResults().get(0).getGeometry().getLocation().getLat();
                                    double longitude = geocode.getResults().get(0).getGeometry().getLocation().getLng();

                                    LatLng propertyLatLng = new LatLng(latitude,longitude);
                                    putMarkerOnPropertyPosition(propertyLatLng,  propertyList.get(finalI));

                                    mHashMap.put(propertyLatLng, propertyList.get(finalI));

                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "on Error :" + e);
                            }

                            @Override
                            public void onComplete() {
                                onMarkerClick(mHashMap);
                            }
                        });
            }
        });
    }

    private void onMarkerClick(HashMap<LatLng, Property> hashMap) {
        mMap.setOnMarkerClickListener(marker -> {
            Log.d(TAG, "marker position :" + marker.getPosition().toString());
            Property property = getPropertyFromLatLng(hashMap, marker.getPosition());

            if (property != null) {
                mMap.setInfoWindowAdapter(new MapCustomWindowAdapter(this, property));
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        mMarker = marker;
                        Intent intent = new Intent(getApplicationContext(), EditPropertyActivity.class);
                        intent.putExtra(PROPERTY_ID_KEY, property.getId());
                        intent.putExtra(PROPERTY_AGENT_ID_KEY, property.getAgentId());
                        startActivityForResult(intent, REQUEST_CODE_EDIT_PROPERTY);

                    }
                });
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_PROPERTY){
            if (resultCode == RESULT_OK){
                if (data != null) {
                    Property property = data.getParcelableExtra(PROPERTY_KEY_MAP_ACTIVITY);
                    // refresh data shown in custom window adapter
                    mMarker.hideInfoWindow();
                    mMap.setInfoWindowAdapter(new MapCustomWindowAdapter(this, property));
                    mMarker.showInfoWindow();


                    if (property != null && property.isAvailable()){
                        mMarker.setIcon((BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    } else if (property != null && !property.isAvailable()){
                        mMarker.setIcon((BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }
            }
        }
    }


    // ------ CONFIGURATION ------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }

    private void putMarkerOnPropertyPosition(LatLng propertyLatLng, Property property) {
        if (property.isAvailable()) {
            mMap.addMarker(new MarkerOptions()
                    .position(propertyLatLng)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        else { // red marker if sold
            mMap.addMarker(new MarkerOptions()
                    .position(propertyLatLng));
        }
    }




    // ------ UTILS ------
    private Property getPropertyFromLatLng(Map<LatLng, Property> map , LatLng propertyPosition){
        for (Map.Entry<LatLng, Property> entry : map.entrySet()){
            if (Objects.equals(propertyPosition, entry.getKey())){
                return entry.getValue();
            }
        }
        return null;
    }

}
