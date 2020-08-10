package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.AddAgentBottomSheetFragment;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;


import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends BaseActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener {
    // ----- FOR DATA -----
    private REMViewModel mPropertyViewModel;

    private static final String READ_EXT_STORAGE_PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXT_STORAGE_PERMS = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMS = Manifest.permission.CAMERA;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;
    public static final int RC_TAKE_PHOTO = 300;

    // ----- FOR UI -----
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mCity, mPrice, mAddress, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface,mEdtTxtNbrRoom,mEdtTxtNbrBedroom,mEdtTxtNbrBathroom;
    private Button mButtonSelectPhoto, mButtonTakePhoto, mButtonChooseAgent;
    private TextView mAgentSelected;

    // ----- LIFECYCLE -----

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



    // ----- CONFIGURATION METHODS -----


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

        mAgentSelected = findViewById(R.id.agent_chosen_add_property_activity);

    }

    // ----- ON CLICK LISTENER -----

    private void onClickChooseAgent() {
        mButtonChooseAgent.setOnClickListener(view -> {
            AddAgentBottomSheetFragment.newInstance().show(getSupportFragmentManager(),"AddAgentBottomSheetFragment");
            Toast.makeText(this, "Open Bottom Sheet Agent", Toast.LENGTH_SHORT).show();
        });
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

            String agentSelected = mAgentSelected.getText().toString().trim(); // TODO: add a string "No agent selected"
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


    @Override
    public void onClickAgentItem(Agent agent) {
        mAgentSelected.setText(agent.getName());
    }


    @AfterPermissionGranted(RC_CHOOSE_PHOTO)
    private void onClickAddPicture() {
        mButtonSelectPhoto.setOnClickListener(view ->{
            if (!EasyPermissions.hasPermissions(this, READ_EXT_STORAGE_PERMS))
            {
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs to access your photo storage",RC_CHOOSE_PHOTO, READ_EXT_STORAGE_PERMS);
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
            if(!EasyPermissions.hasPermissions(this, CAMERA_PERMS, WRITE_EXT_STORAGE_PERMS)){
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs your permission to use camera", RC_TAKE_PHOTO, CAMERA_PERMS, WRITE_EXT_STORAGE_PERMS);
                return;
            }
            takePictureIntent();

        });

    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("Photo Path", "Photo Path :" + currentPhotoPath);
        return image;
    }


    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("AddPropertyActivity", "Create File" + photoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("AddPropertyActivity", "Error while creating the File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.openclassrooms.realestatemanager.fileprovider",
                            photoFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, RC_TAKE_PHOTO);

                } catch (Exception e){
                    Log.e("AddPropertyActivity","File path exception:" + e);
                }
            }
        }
    }

    // ----- ON ACTIVITY RESULT -----

    private void addPictureToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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
                addPictureToGallery();
                setPic();

                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = 300;
        int targetH = 300;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);


        ImageView imageView = new ImageView(this);

        imageView.setImageBitmap(bitmap);


        addPhotoLinearLayout(imageView);
    }

    private void addPhotoLinearLayout(ImageView imageView) {
        LinearLayout layout = findViewById(R.id.linear_layout_photo_add_activity);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setPadding(20,20,20,20);
        layout.addView(imageView);
        /*
        for (int i = 0; i < 100 ; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(20, 20, 20, 20);


            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.ic_baseline_add_24));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }

         */
    }

}
