package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
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
import pub.devrel.easypermissions.EasyPermissions;

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
    private FusedLocationProviderClient mFusedLocationProvider;

    // ------ LIFE CYCLE ------
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configure map
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        API_KEY = getString(R.string.api_key);
        configureViewModel();

        mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(this);
        askGpsPermission();
    }

    @SuppressLint("MissingPermission")
    private void askGpsPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            getDeviceLocation();
        } else{
            EasyPermissions.requestPermissions(this,"SolarTime needs your position to calculate", 123, permissions);
        }
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
        getDeviceLocation();
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
                mMap.setOnInfoWindowClickListener(marker1 -> {
                    mMarker = marker1;
                    Intent intent = new Intent(getApplicationContext(), EditPropertyActivity.class);
                    intent.putExtra(PROPERTY_ID_KEY, property.getId());
                    intent.putExtra(PROPERTY_AGENT_ID_KEY, property.getAgentId());
                    startActivityForResult(intent, REQUEST_CODE_EDIT_PROPERTY);

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

    // ------ DEVICE LOCATION METHODS ------

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, permissions)) {
            mFusedLocationProvider.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    mMap.setMyLocationEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()), 12f));

                    writeLastKnownLocation(location);

                } else {
                    readLastKnownLocation();
                }
            });
        }
    }

    private void writeLastKnownLocation(Location deviceLocation){
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        deviceLocation.getLongitude();
        pref.edit().putString("device_latitude", String.valueOf(deviceLocation.getLatitude())).apply();
        pref.edit().putString("device_longitude", String.valueOf(deviceLocation.getLongitude())).apply();
    }
    private void readLastKnownLocation(){
        String lat = this.getPreferences(Context.MODE_PRIVATE).getString("device_latitude", "48.8534");
        String lng = this.getPreferences(Context.MODE_PRIVATE).getString("device_longitude", "2.3488");
        if (lat != null && lng != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)), 12f));
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
