package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.MyTextWatcher;

import java.util.Date;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;

public class EditPropertyActivity extends BaseActivity{

    private Property mProperty;
    private LiveData<Property> liveDataProperty;
    private EditText mEdtTxtPrice, mEdtTxtAddress, mEdtTxtCity, mEdtTxtSurface, mEdtTxtNbrOfRoom, mEdtTxtNbrOfBedroom,
            mEdtTxtNbrOfBathroom, mEdtTxtDescription;
    private Spinner mTypeSpinner;
    private Date mDateAvailable;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        configureLiveData();
        configureViews();
        configureSpinnerType();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            long propertyId = bundle.getLong(PROPERTY_ID_KEY);
            long agentId = bundle.getLong(PROPERTY_AGENT_ID_KEY);

            System.out.println("EditActivity propertyId: " + propertyId + " + agentId :" + agentId);

            liveDataProperty = mEditPropertyViewModel.getProperty(propertyId, agentId);

            if (savedInstanceState == null){ //get data from database once and if screen rotation, don't fetch data again
                liveDataProperty.observe(this, property -> {
                    mEdtTxtPrice.setText(property.getPrice());
                    mEdtTxtAddress.setText(property.getAddress());
                    mEdtTxtCity.setText(property.getCity());
                    mEdtTxtSurface.setText(property.getSurface());
                    mEdtTxtNbrOfRoom.setText(property.getNumberOfRooms());
                    mEdtTxtNbrOfBedroom.setText(property.getNumberOfBedrooms());
                    mEdtTxtNbrOfBathroom.setText(property.getNumberOfBathRooms());
                    mEdtTxtDescription.setText(property.getDescription());

                    mProperty = property;

                    System.out.println("TEST onCreate :" + mProperty.getPrice());
                });

            }

            mEdtTxtPrice.addTextChangedListener(new MyTextWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    if (mProperty != null){
                        mProperty.setPrice(editable.toString());
                    }
                }
            });

        }

    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putParcelable("KEY", mProperty);

        System.out.println("TEST onSave :" + mProperty.getPrice());
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
        mProperty = savedInstanceState.getParcelable("KEY");

        System.out.println("TEST onRestore :" + mProperty.toString());
        mEdtTxtPrice.setText(mProperty.getPrice());
        mEdtTxtAddress.setText(mProperty.getAddress());
        mEdtTxtCity.setText(mProperty.getCity());
        mEdtTxtSurface.setText(mProperty.getSurface());
        mEdtTxtNbrOfRoom.setText(mProperty.getNumberOfRooms());
        mEdtTxtNbrOfBedroom.setText(mProperty.getNumberOfBedrooms());
        mEdtTxtNbrOfBathroom.setText(mProperty.getNumberOfBathRooms());
        mEdtTxtDescription.setText(mProperty.getDescription());
    }


 */


    private void configureLiveData() {
        LiveData<String> liveDataPrice, liveDataAddress, liveDataCity; // etc
        liveDataPrice = mEditPropertyViewModel.getPrice();
        liveDataPrice.observe(this, s -> {
            System.out.println("coucou" + s);
        });

    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.edit_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.add_property_activity_spinner_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
    }

    private void configureViews() {
        mEdtTxtPrice = findViewById(R.id.editText_price_edit_activity);
        mEdtTxtAddress = findViewById(R.id.editText_address_edit_activity);
        mEdtTxtCity = findViewById(R.id.editText_city_edit_activity);
        mEdtTxtSurface = findViewById(R.id.editText_surface_edit_activity);
        mEdtTxtNbrOfRoom = findViewById(R.id.editText_number_of_room_edit_activity);
        mEdtTxtNbrOfBedroom = findViewById(R.id.editText_number_of_bedroom_edit_activity);
        mEdtTxtNbrOfBathroom = findViewById(R.id.editText_number_of_bathroom_edit_activity);
        mEdtTxtDescription = findViewById(R.id.editText_description_edit_activity);

        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_edit_activity);
        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);
    }

}