package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;

public class PropertyDetailActivity extends AppCompatActivity {

    // FOR DATA
    public static final String PROPERTY_CITY = "PROPERTY CITY";
    public static final String PROPERTY_PRICE = "PROPERTY PRICE";

    // FOR UI
    TextView mPropertyCity, mPropertyPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_property_detail);


        Intent intent = getIntent();

        String propertyCity = intent.getStringExtra(PROPERTY_CITY);
        int propertyPrice = intent.getIntExtra(PROPERTY_PRICE,0);

        mPropertyCity = findViewById(R.id.detail_activity_property_city);
        mPropertyPrice = findViewById(R.id.detail_fragment_property_price);

        mPropertyCity.setText(propertyCity);
        mPropertyPrice.setText(String.valueOf(propertyPrice));
    }
}
