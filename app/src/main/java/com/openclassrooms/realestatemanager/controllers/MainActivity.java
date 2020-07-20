package com.openclassrooms.realestatemanager.controllers;


import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.views.PropertiesAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PropertiesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureRecyclerView();
    }


    private void configureRecyclerView(){
        mRecyclerView = findViewById(R.id.properties_recycler_view);

        List<Property> propertiesList = new ArrayList<>();
        propertiesList.add(new Property("Paris",123));
        propertiesList.add(new Property("Marseille",456));
        propertiesList.add(new Property("Toulouse",789));
        propertiesList.add(new Property("Rennes",1011));
        propertiesList.add(new Property("Lille",1213));
        propertiesList.add(new Property("Strasbourg",1415));
        propertiesList.add(new Property("Montpellier",1617));
        propertiesList.add(new Property("Strasbourg",1819));
        propertiesList.add(new Property("Troyes",2021));
        propertiesList.add(new Property("Dijon",2223));


        mAdapter = new PropertiesAdapter(propertiesList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

    }

}
