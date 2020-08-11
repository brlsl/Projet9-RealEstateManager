package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends BaseActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener {
    // ----- FOR DATA -----
    private REMViewModel mPropertyViewModel;

    private static final String READ_EXT_STORAGE_PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXT_STORAGE_PERMS = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMS = Manifest.permission.CAMERA;
    private static final int RC_CHOOSE_PHOTO = 100;
    public static final int RC_TAKE_PHOTO = 200;

    // ----- FOR UI -----
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mCity, mPrice, mAddress, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface,mEdtTxtNbrRoom,mEdtTxtNbrBedroom,mEdtTxtNbrBathroom, mEdtTxtDescription;
    private Button mButtonSelectPhoto, mButtonTakePhoto, mButtonChooseAgent, mButtonAvailabilityDate;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewAvailabilityDate;
    private int mNumberOfPictureAdded = 0;
    private long mSelectedAgentId = -1;
    private boolean isAvailable = true;

    // ----- LIFECYCLE -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViewModel();
        configureSpinnerType();
        configureViews();
        
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

    private void configureViews() {
        mEdtTxtCity = findViewById(R.id.editText_city_add_activity);
        mEdtTxtPrice = findViewById(R.id.editText_price_add_activity);
        mEdtTxtAddress = findViewById(R.id.editText_address_add_activity);
        mEdtTxtSurface = findViewById(R.id.editText_surface_add_activity);
        mEdtTxtNbrRoom = findViewById(R.id.editText_number_of_room_add_activity);
        mEdtTxtNbrBedroom = findViewById(R.id.editText_number_of_bedroom_add_activity);
        mEdtTxtNbrBathroom = findViewById(R.id.editText_number_of_bathroom_add_activity);
        mEdtTxtDescription = findViewById(R.id.editText_description_add_activity);

        mAddPropertyButton = findViewById(R.id.activity_add_property_button);

        mButtonSelectPhoto = findViewById(R.id.button_select_picture_add_activity);
        mButtonTakePhoto = findViewById(R.id.button_take_picture_add_activity);
        mButtonChooseAgent = findViewById(R.id.choose_agent_button_add_activity);
        mButtonAvailabilityDate = findViewById(R.id.button_avaibility_date_add_activity);

        mTxtViewAgent = findViewById(R.id.agent_chosen_add_property_activity);
        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_add_activity);
        mTxtViewAvailabilityDate = findViewById(R.id.textView__avaibility_date_add_property_activity);
        descriptionTitleLengthListener();
        setDatePickerDialog();
    }

    private void descriptionTitleLengthListener() {
        mEdtTxtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int maxLength = 300;
                mTxtViewDescriptionTitle.setText("Description (" + (maxLength - mEdtTxtDescription.length()) + "/300)" );
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
            mType = mTypeSpinner.getSelectedItem().toString();
            mPrice = mEdtTxtPrice.getText().toString().trim();
            mAddress = mEdtTxtAddress.getText().toString().trim();
            mCity = mEdtTxtCity.getText().toString().trim();
            mSurface = mEdtTxtSurface.getText().toString().trim();
            mNbrOfRoom = mEdtTxtNbrRoom.getText().toString().trim();
            mNbrOfBedroom = mEdtTxtNbrBedroom.getText().toString().trim();
            mNbrOfBathroom = mEdtTxtNbrBathroom.getText().toString().trim();
            mDescription = mEdtTxtDescription.getText().toString();

            String agentSelected = mTxtViewAgent.getText().toString().trim();
            String dateAvailable = mTxtViewAvailabilityDate.getText().toString().trim();

            if (mSelectedAgentId == -1 || mType.isEmpty() || mPrice.isEmpty() || mAddress.isEmpty() || mCity.isEmpty() ||
                    mSurface.isEmpty() || mNbrOfRoom.isEmpty() || mNbrOfBedroom.isEmpty() || mNbrOfBathroom.isEmpty() || mDescription.isEmpty() ||
                    mNumberOfPictureAdded == 0 || agentSelected.isEmpty() || dateAvailable.isEmpty()
            )
                Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
            else {
                Property propertyAdded = new Property(mSelectedAgentId,
                        mCity,
                        mType,
                        mAddress,
                        Integer.parseInt(mPrice),
                        Integer.parseInt(mSurface),
                        Integer.parseInt(mNbrOfRoom),
                        Integer.parseInt(mNbrOfBedroom),
                        Integer.parseInt(mNbrOfBathroom),
                        mDescription,
                        dateAvailable,
                        isAvailable);
                mPropertyViewModel.createProperty(propertyAdded);
                finish();
                Toast.makeText(AddPropertyActivity.this, "Property added", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClickAgentItem(Agent agent) {
        mTxtViewAgent.setText(agent.getName() +" " + agent.getSurname());
        mSelectedAgentId = agent.getId();
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
    String selectedImagePath;

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

    private void addPhotoToGallery() {
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
            if (resultCode == RESULT_OK && data != null){


                Uri uri = data.getData();
                ImageView imageView = new ImageView(this);
                imageView.setImageURI(uri);
                selectedImagePath = getPath(uri);
                setPicture(selectedImagePath); // and add to linear layout

                Log.e("Tag", "Selected picture path:" + selectedImagePath);

                Toast.makeText(this, "Picture Selected", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Picture has not been selected", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == RC_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                addPhotoToGallery();
                setPicture(currentPhotoPath);

                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPicture(String photoPath) {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 400;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        ImageView imageView = new ImageView(this);

        imageView.setImageBitmap(bitmap);

        addPhotoToLinearLayout(imageView);
    }

    private void addPhotoToLinearLayout(ImageView imageView) {
        LinearLayout layout = findViewById(R.id.linear_layout_photo_add_activity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5,5,5,5);
        layout.addView(imageView);
        Log.e("Tag", "Number of picture in Linear Layout : " + layout.getChildCount());
        mNumberOfPictureAdded = layout.getChildCount();
    }

    //helper to retrieve the path of an image URI
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null)
        {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }


    private void setDatePickerDialog(){
        mButtonAvailabilityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentYear, currentMonth, currentDayOfMonth ;
                Calendar calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPropertyActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        if(dayOfMonth <10 && month < 10)
                            mTxtViewAvailabilityDate.setText("0"+ dayOfMonth +"/" + "0"+(month + 1) + "/"+ year);
                        else if (dayOfMonth < 10)
                            mTxtViewAvailabilityDate.setText("0"+ dayOfMonth +"/" +(month + 1) + "/"+ year);
                        else if (month < 10)
                            mTxtViewAvailabilityDate.setText(dayOfMonth +"/" +"0"+(month + 1) + "/"+ year);
                        else
                            mTxtViewAvailabilityDate.setText(dayOfMonth +"/" +(month + 1) + "/"+ year);
                    }
                }, currentYear, currentMonth, currentDayOfMonth);
                datePickerDialog.show();
            }
        });
    }
}
