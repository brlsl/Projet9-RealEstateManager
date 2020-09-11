package com.openclassrooms.realestatemanager.controllers.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends BasePropertyActivity {

    // FOR DATA
    private static final String TAG = "AddPropertyActivity";
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private List<String> mImagePathList = new ArrayList<>(), mPointsOfInterestList = new ArrayList<>();
    private long mAgentId;
    private boolean isAvailable = true;
    private String mAgentNameSurname;

    // ----- FOR UI -----
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mPrice, mAddress, mCity, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface, mEdtTxtNbrRoom, mEdtTxtNbrBedroom, mEdtTxtNbrBathroom, mEdtTxtDescription;
    private ImageButton mImgBtnChoosePicture, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailableDate;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable;
    private Date mDateAvailable, mDateSold = new Date();
    private CheckBox mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant, mCckBoxMall, mCckBoxCinema, mCckBoxPark;
    private LinearLayout mLinearLayout;

    // ----- LIFECYCLE -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        configureViews();
        configureLiveData();
        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);
        configureSpinnerType();

        onClickCheckBoxes(mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant,mCckBoxMall, mCckBoxCinema,
                mCckBoxPark, mPointsOfInterestList, mPropertyActivityViewModel);
        onClickAddProperty();

        onClickChooseAgent(mImgBtnChooseAgent);
        onClickTakePicture();
        onClickChoosePicture();
        onClickAvailableDatePicker(mImgBtnAvailableDate, mPropertyActivityViewModel);
    }

    @AfterPermissionGranted(RC_CHOOSE_PHOTO)
    void onClickChoosePicture() {
        mImgBtnChoosePicture.setOnClickListener(view ->{
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
    void onClickTakePicture(){
        mImgBtnTakePhoto.setOnClickListener(view ->{
            if(!EasyPermissions.hasPermissions(this, CAMERA_PERMS)){
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs your permission to use camera", RC_TAKE_PHOTO, CAMERA_PERMS);
                return;
            }
            takePictureIntent();
        });

    }

    // ----- CONFIGURATION METHODS -----

    @SuppressLint("ResourceType")
    private void configureLiveData() {
        LiveData<Date> dateLiveData = mPropertyActivityViewModel.getDateAvailable();
        LiveData<List<Bitmap>> bitmapLiveData = mPropertyActivityViewModel.getBitmapList();
        LiveData<Agent> agentLiveData = mPropertyActivityViewModel.getAgentMutableLiveData();
        LiveData<List<String>> pathListLiveData = mPropertyActivityViewModel.getPathList();
        LiveData<List<String>> pointOfInterestLiveData = mPropertyActivityViewModel.getPointsOfInterestList();

        agentLiveData.observe(this, agent -> {
            mAgentNameSurname = agent.getName()+" "+agent.getSurname();
            mTxtViewAgent.setText(mAgentNameSurname);
            mAgentId = agent.getId();
            Log.e(TAG, "LiveData name value: " +mAgentNameSurname + ", TextView name value: "+ mTxtViewAgent.getText().toString() + ", agent Id value: " + mAgentId );
        });

        dateLiveData.observe(this, date -> {
            mDateAvailable = date;
            mTxtViewDateAvailable.setText(Utils.formatDateToString(mDateAvailable));
            Log.e(TAG, "LiveData date value: " + mDateAvailable + " LiveData TextView date value: "+ mTxtViewDateAvailable.getText().toString());
        });

        bitmapLiveData.observe(this, bitmapList -> {
            mLinearLayout.removeAllViews();
            for (int i = 0; i < bitmapList.size() ; i++) {
                mBitmapList = bitmapList;
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmapList.get(i));
                imageView.setPadding(5,20,5,20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                imageView.setLayoutParams(params);

                ImageButton deleteButton = new ImageButton(this);
                deleteButton.setImageResource(R.drawable.ic_baseline_delete_24);
                deleteButton.setBackgroundColor(Color.TRANSPARENT);

                imageView.setId(i+1); // avoid id = 0
                deleteButton.setId(i+1);
                mLinearLayout.addView(deleteButton, i);
                mLinearLayout.addView(imageView);
                deleteButton.setX(-100);
                deleteButton.bringToFront();

                deleteButton.setOnClickListener(view -> {
                    mLinearLayout.removeView(view);
                    mLinearLayout.removeView(imageView);
                    mBitmapList.remove(imageView.getId()-1);
                    mPropertyActivityViewModel.getBitmapList().setValue(mBitmapList);
                    mImagePathList.remove(imageView.getId()-1);
                    mPropertyActivityViewModel.getPathList().setValue(mImagePathList);
                });

                Log.e(TAG, "image id: " + imageView.getId() );
                Log.e(TAG, "Number in bitmap List: " + mBitmapList.size() );
                Log.e(TAG, "Number of photo in linearlayout: " + mLinearLayout.getChildCount() );
            }

        });

        pathListLiveData.observe(this, strings -> {
            mImagePathList = strings;
            Log.e(TAG, "Number in path List data: " + mImagePathList.size() );
        });

        pointOfInterestLiveData.observe(this, strings -> {
            mPointsOfInterestList = strings;
            System.out.println(TAG + "point of Interest list " + mPointsOfInterestList.size());
        } );
    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
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

        mImgBtnChoosePicture = findViewById(R.id.button_choose_picture_add_activity);
        mImgBtnTakePhoto = findViewById(R.id.button_take_picture_add_activity);
        mImgBtnChooseAgent = findViewById(R.id.choose_agent_button_add_activity);
        mImgBtnAvailableDate = findViewById(R.id.button_availability_date_add_activity);

        mTxtViewAgent = findViewById(R.id.agent_chosen_add_property_activity);
        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_add_activity);
        mTxtViewDateAvailable = findViewById(R.id.textView_availability_date_add_property_activity);
        mLinearLayout = findViewById(R.id.linear_layout_photo_add_activity);

        mCckBoxSchool = findViewById(R.id.checkBox_school_add_activity);
        mCckBoxHospital = findViewById(R.id.checkBox_hospital_add_activity);
        mCckBoxRestaurant = findViewById(R.id.checkBox_restaurant_add_activity);
        mCckBoxMall = findViewById(R.id.checkBox_mall_add_activity);
        mCckBoxCinema = findViewById(R.id.checkBox_cinema_add_activity);
        mCckBoxPark = findViewById(R.id.checkBox_park_add_activity);

    }


    // ----- ON CLICK LISTENER -----


    @Override
    public void onClickAgentItem(Agent agent) {
        mPropertyActivityViewModel.getAgentMutableLiveData().setValue(agent);
    }


    private void onClickAddProperty() {
        mAddPropertyButton.setOnClickListener(view -> {
            if (!isPossibleToAddProperty())
            {
                Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
            }
            else {
                Property propertyAdded = new Property(mAgentId, mCity, mType, mAddress, mPrice, mSurface, mNbrOfRoom, mNbrOfBedroom,
                        mNbrOfBathroom, mDescription, mDateAvailable, mDateSold, mAgentNameSurname, mPointsOfInterestList,mImagePathList.get(0), isAvailable);
                mPropertyActivityViewModel.createProperty(propertyAdded);

                // wait between property creation and get all properties from database
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mPropertyActivityViewModel.getPropertyList().observe(AddPropertyActivity.this, properties -> {
                    long propertyId = properties.get(properties.size() -1).getId();

                    // write image path in DB
                    for (int i = 0; i < mImagePathList.size() ; i++) {
                        Image image = new Image(propertyId, mImagePathList.get(i));
                        mPropertyActivityViewModel.createImage(image);
                        Log.e(TAG,"contenu de la liste: " + mImagePathList.get(i) + " " + propertyId);

                    }
                });

                Log.e(TAG, "Contenu de Image List: " + mImagePathList);
                finish();
                Toast.makeText(AddPropertyActivity.this, "Property added date" + mDateAvailable +" et" + mAgentNameSurname, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----- ON ACTIVITY RESULT -----

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK && data != null && data.getData() != null){
                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),data.getData());
                            Bitmap originalBitmap = ImageDecoder.decodeBitmap(source);

                            //resize original bitmap for linear layout
                            Bitmap resizedBitmap = resizeBitmapForLinearLayout(originalBitmap, mLinearLayout);

                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                            File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"JPEG_" + timeStamp + "_");

                            createBitmapAtFilePath(selectedImgFile, originalBitmap);

                            mBitmapList.add(resizedBitmap);
                            mPropertyActivityViewModel.getBitmapList().setValue(mBitmapList);

                            mImagePathList.add(selectedImgFile.getAbsolutePath());
                            mPropertyActivityViewModel.getPathList().setValue(mImagePathList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                    {
                        Uri uri = data.getData();

                        mChosenPhotoPath = getRealPathFromUri(uri);
                        mPropertyActivityViewModel.getChosenPhotoPath().setValue(mChosenPhotoPath);
                        setPictureFromPath(mChosenPhotoPath, mBitmapList, mImagePathList); // and add to linear layout

                        Log.e(TAG, "Selected picture path:" + mChosenPhotoPath);

                        Toast.makeText(this, "Picture Selected", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e(TAG,"Exception photo choose: "+ e);
                }

            } else{
                Toast.makeText(this, "Picture has not been selected", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == RC_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                try {
                    setPictureFromPath(mTakenPhotoPath, mBitmapList, mImagePathList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPossibleToAddProperty(){
        mType = mTypeSpinner.getSelectedItem().toString();
        mPrice = mEdtTxtPrice.getText().toString().trim();
        mAddress = mEdtTxtAddress.getText().toString().trim();
        mCity = mEdtTxtCity.getText().toString().trim();
        mSurface = mEdtTxtSurface.getText().toString().trim();
        mNbrOfRoom = mEdtTxtNbrRoom.getText().toString().trim();
        mNbrOfBedroom = mEdtTxtNbrBedroom.getText().toString().trim();
        mNbrOfBathroom = mEdtTxtNbrBathroom.getText().toString().trim();
        mDescription = mEdtTxtDescription.getText().toString();

        if (mType.isEmpty() || mPrice.isEmpty() || mAddress.isEmpty() || mCity.isEmpty() ||
                mSurface.isEmpty() || mNbrOfRoom.isEmpty() || mNbrOfBedroom.isEmpty() || mNbrOfBathroom.isEmpty() || mDescription.isEmpty() ||
                mBitmapList.size() == 0 || mTxtViewAgent.getText().length() == 0 || mDateAvailable == null) {
            return false;
        }
        else return true;
    }
}
