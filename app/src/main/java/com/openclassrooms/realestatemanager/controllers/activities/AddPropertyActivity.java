package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.PropertyViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;

public class AddPropertyActivity extends AppCompatActivity {

    PropertyViewModel mPropertyViewModel;

    // FOR DATA


    // FOR UI
    EditText editTextPrice, editTextCity;
    Button addPropertyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViewModel();

        Agent agent = new Agent(1,"LSL","Br");
        mPropertyViewModel.createAgent(agent);

        editTextCity = findViewById(R.id.editText_city);
        editTextPrice = findViewById(R.id.editText_price);

        addPropertyButton = findViewById(R.id.activity_add_property_button);

        addPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editTextCity.getText().toString().trim();
                String price = editTextPrice.getText().toString().trim();
                if (city.equals("") || price.equals("")){

                    Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();

                }
                else {

                    Property propertyAdded = new Property(1, city,"apartment", Integer.parseInt(price),
                            50,1,1,1);
                    mPropertyViewModel.createProperty(propertyAdded);
                    Toast.makeText(AddPropertyActivity.this, "Property added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
        mPropertyViewModel.init(); // get all properties
    }

}
