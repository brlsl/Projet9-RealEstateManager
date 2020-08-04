package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;

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


import com.openclassrooms.realestatemanager.models.Property;


public class AddPropertyActivity extends AppCompatActivity{

    // FOR DATA
    private PropertyViewModel mPropertyViewModel;

    // FOR UI
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mCity, mPrice, mAddress, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface,mEdtTxtNbrRoom,mEdtTxtNbrBedroom,mEdtTxtNbrBathroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViewModel();
        configureSpinnerType();
        configureFields();
        addOnPropertyButtonListener();

    }

    private void addOnPropertyButtonListener() {
        mAddPropertyButton.setOnClickListener(view -> {
            mCity = mEdtTxtCity.getText().toString().trim();
            mPrice = mEdtTxtPrice.getText().toString().trim();
            mAddress = mEdtTxtAddress.getText().toString().trim();
            mSurface = mEdtTxtSurface.getText().toString().trim();
            mNbrOfRoom = mEdtTxtNbrRoom.getText().toString().trim();
            mNbrOfBedroom = mEdtTxtNbrBedroom.getText().toString().trim();
            mNbrOfBathroom = mEdtTxtNbrBathroom.getText().toString().trim();

            mType = mTypeSpinner.getSelectedItem().toString();

            if (mCity.isEmpty() || mPrice.isEmpty() || mAddress.isEmpty() || mType.isEmpty() ||
                    mSurface.isEmpty() || mNbrOfRoom.isEmpty() || mNbrOfBedroom.isEmpty() || mNbrOfBathroom.isEmpty())
                Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
            else {
                Property propertyAdded = new Property(1,
                        mCity,
                        mType,
                        Integer.parseInt(mPrice),
                        Integer.parseInt(mSurface),
                        Integer.parseInt(mNbrOfRoom),
                        Integer.parseInt(mNbrOfBedroom),
                        Integer.parseInt(mNbrOfBathroom));
                mPropertyViewModel.createProperty(propertyAdded);
                finish();
                Toast.makeText(AddPropertyActivity.this, "Property added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureFields() {
      mEdtTxtCity = findViewById(R.id.editText_city_add_activity);
      mEdtTxtPrice = findViewById(R.id.editText_price_add_activity);
      mEdtTxtAddress = findViewById(R.id.editText_address_add_activity);
      mEdtTxtSurface = findViewById(R.id.editText_surface_add_activity);
      mEdtTxtNbrRoom = findViewById(R.id.editText_number_of_room_add_activity);
      mEdtTxtNbrBedroom = findViewById(R.id.editText_number_of_bedroom_add_activity);
      mEdtTxtNbrBathroom = findViewById(R.id.editText_number_of_bathroom_add_activity);

      mAddPropertyButton = findViewById(R.id.activity_add_property_button);

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
    

    private Boolean isPossibleAdding(String city, String address) {
        return !city.isEmpty() && !address.isEmpty();
    }

}
