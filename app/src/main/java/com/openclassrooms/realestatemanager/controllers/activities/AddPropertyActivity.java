package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.PropertyViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;

public class AddPropertyActivity extends AppCompatActivity{

    PropertyViewModel mPropertyViewModel;

    // FOR DATA


    // FOR UI
    EditText mEditTextPrice, mEditTextCity;
    Button mAddPropertyButton;
    Spinner mTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViewModel();

        mEditTextCity = findViewById(R.id.editText_city);
        mEditTextPrice = findViewById(R.id.editText_price);

        configureSpinnerType();

        mAddPropertyButton = findViewById(R.id.activity_add_property_button);
        mAddPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = mEditTextCity.getText().toString().trim();
                String price = mEditTextPrice.getText().toString().trim();
                String type = mTypeSpinner.getSelectedItem().toString().trim();
                if (city.equals("") || price.equals("") || type.equals("")){
                    Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
                }
                else {
                    Property propertyAdded = new Property(1, city,type, Integer.parseInt(price),
                            50,1,1,1);
                    mPropertyViewModel.createProperty(propertyAdded);
                    finish();
                    Toast.makeText(AddPropertyActivity.this, "Property added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(PropertyViewModel.class);
    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
    }


}
