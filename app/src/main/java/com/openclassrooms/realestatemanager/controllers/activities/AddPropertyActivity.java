package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.AddAgentBottomSheetFragment;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;


import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends BaseActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener {
    // FOR DATA
    private REMViewModel mPropertyViewModel;

    private static final String READ_EXT_STORAGE_PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMS = Manifest.permission.CAMERA;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    public static final int RC_TAKE_PHOTO = 300;

    // FOR UI
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mCity, mPrice, mAddress, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface,mEdtTxtNbrRoom,mEdtTxtNbrBedroom,mEdtTxtNbrBathroom;
    private Button mButtonSelectPhoto, mButtonTakePhoto, mButtonChooseAgent;
    private TextView mAgentChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViewModel();
        configureSpinnerType();
        configureFields();
        
        onClickAddPicture();
        onClickAddProperty();
        onClickTakePicture();
        onClickChooseAgent();
    }

    private void onClickChooseAgent() {
        mButtonChooseAgent.setOnClickListener(view -> {
            AddAgentBottomSheetFragment.newInstance().show(getSupportFragmentManager(),"AddAgentBottomSheetFragment");
            Toast.makeText(this, "Open Bottom Sheet Agent", Toast.LENGTH_SHORT).show();
        });
    }

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private void onClickAddPicture() {
        mButtonSelectPhoto.setOnClickListener(view ->{
            if (!EasyPermissions.hasPermissions(this, READ_EXT_STORAGE_PERMS))
            {
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs to access your photo storage",RC_IMAGE_PERMS, READ_EXT_STORAGE_PERMS);
                return;
            }
            // Intent for Selection Image Activity
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RC_CHOOSE_PHOTO);
        });
    }

    @AfterPermissionGranted(RC_TAKE_PHOTO)
    private void onClickTakePicture(){
        mButtonTakePhoto.setOnClickListener(view ->{
            if(!EasyPermissions.hasPermissions(this, CAMERA_PERMS)){
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs your permission to use camera", RC_TAKE_PHOTO, CAMERA_PERMS);
                return;
            }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, RC_TAKE_PHOTO);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Picture Selected", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Picture has not been selected", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == RC_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void onClickAddProperty() {
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

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
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

        mButtonSelectPhoto = findViewById(R.id.button_select_picture_add_activity);
        mButtonTakePhoto = findViewById(R.id.button_take_picture_add_activity);
        mButtonChooseAgent = findViewById(R.id.choose_agent_button_add_activity);

        mAgentChosen = findViewById(R.id.agent_chosen_add_property_activity);

    }

    @Override
    public void onClickAgentItem(Agent agent) {
        mAgentChosen.setText(agent.getName());
    }
}
