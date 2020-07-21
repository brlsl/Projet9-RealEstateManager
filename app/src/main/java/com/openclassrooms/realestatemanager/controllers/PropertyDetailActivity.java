package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;

import org.w3c.dom.Text;

public class PropertyDetailActivity extends AppCompatActivity {

    // FOR DATA
    public static final String PROPERTY_CITY = "property_city";
    public static final String PROPERTY_PRICE = "property_price";

    // FOR UI
    TextView mPropertyCity, mPropertyPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_detail_activity);

        Intent intent = getIntent();

        String propertyCity = intent.getStringExtra(PROPERTY_CITY);
        int propertyPrice = intent.getIntExtra(PROPERTY_PRICE,0);

        mPropertyCity = findViewById(R.id.detail_activity_property_city);
        mPropertyPrice = findViewById(R.id.detail_activit_property_price);

        mPropertyCity.setText(propertyCity);
        mPropertyPrice.setText(String.valueOf(propertyPrice));
    }
}
