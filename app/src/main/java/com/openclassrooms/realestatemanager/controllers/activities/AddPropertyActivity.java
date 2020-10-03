package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;


import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.controllers.fragments.AddAgentBottomSheetFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.AddPhotoTitleDialogFragment;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.views.PropertyImageList.PropertyImageAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.controllers.activities.SettingsActivity.SettingsFragment.PREFERENCES_CURRENCY;


public class AddPropertyActivity extends BasePropertyActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener, AddPhotoTitleDialogFragment.OnClickDialogConfirmListener {

    // FOR DATA
    private static final String TAG = "AddPropertyActivity";
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private PropertyImageAdapter mPropertyImageAdapter;
    private List<String> mImagePathList = new ArrayList<>(),  mImageTitleList = new ArrayList<>(), mPointsOfInterestList = new ArrayList<>();
    private long mAgentId;
    private boolean isAvailable = true;
    private String mAgentNameSurname;

    // ----- FOR UI -----
    private ScrollView mScrollView;
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mPrice, mAddress, mCity, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface, mEdtTxtNbrRoom, mEdtTxtNbrBedroom, mEdtTxtNbrBathroom, mEdtTxtDescription;
    private ImageButton mImgBtnChoosePicture, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailableDate;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable, mTxtViewCurrency;
    private Date mDateAvailable, mDateSold = new Date();
    private CheckBox mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant, mCckBoxMall, mCckBoxCinema, mCckBoxPark;
    private RecyclerView mRecyclerView;
    // ----- LIFECYCLE -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);
        setTitle("Add Property");


        configureViews();
        configureCurrenryField();
        configureLiveData();

        configureRecyclerViewPhoto();
        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);
        configureSpinnerType();

        onClickCheckBoxes(mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant,mCckBoxMall, mCckBoxCinema,
                mCckBoxPark, mPointsOfInterestList, mPropertyActivityViewModel);
        onClickAddProperty();

        onClickChooseAgent(mImgBtnChooseAgent);
        onClickTakePicture();
        onClickChoosePicture();
        onClickDatePicker(mImgBtnAvailableDate, mPropertyActivityViewModel);

    }

    private void configureCurrenryField() {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDollar = preferences.getBoolean(PREFERENCES_CURRENCY,false);
        if (isDollar){
            mTxtViewCurrency.setText(this.getString(R.string.dollar));
        } else
        {
            mTxtViewCurrency.setText(this.getString(R.string.euro));
        }

    }

    private void configureRecyclerViewPhoto() {
        mRecyclerView = findViewById(R.id.recycler_view_photo_add_activity);
        mPropertyImageAdapter = new PropertyImageAdapter(mBitmapList, mImageTitleList, mImagePathList, getSupportFragmentManager(), getApplicationContext());
        mRecyclerView.setAdapter(mPropertyImageAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

    private void configureLiveData() {
        LiveData<Date> dateLiveData = mPropertyActivityViewModel.getDateAvailable();
        LiveData<List<Bitmap>> bitmapLiveData = mPropertyActivityViewModel.getBitmapList();
        LiveData<Agent> agentLiveData = mPropertyActivityViewModel.getAgentMutableLiveData();
        LiveData<List<String>> pointOfInterestLiveData = mPropertyActivityViewModel.getPointsOfInterestList();
        LiveData<List<String>> pathListLiveData = mPropertyActivityViewModel.getPathList();
        MutableLiveData<List<String>> imageTitleListLiveData = mPropertyActivityViewModel.getImageTitleList();
        LiveData<String> currencyLiveData = mPropertyActivityViewModel.getCurrency();


        pathListLiveData.observe(this, strings -> {
            mPropertyImageAdapter.updatePathList(strings);
            mImagePathList = strings;
        });

        bitmapLiveData.observe(this, bitmapList -> {
            mPropertyImageAdapter.updatePhotoList(bitmapList);
            mBitmapList = bitmapList;

            Log.d(TAG, "Number in bitmap List: " + mBitmapList.size() );
            Log.d(TAG, "image title list size: " + mImageTitleList.size());

        });

        imageTitleListLiveData.observe(this, strings -> {
            mPropertyImageAdapter.updateTitleList(strings);
            mImageTitleList = strings;

            Log.d(TAG, "image title List size: " + mImageTitleList.size());
        });

        agentLiveData.observe(this, agent -> {
            mAgentNameSurname = agent.getName()+" "+agent.getSurname();
            mTxtViewAgent.setText(mAgentNameSurname);
            mAgentId = agent.getId();
            Log.d(TAG, "LiveData name value: " +mAgentNameSurname + ", TextView name value: "+ mTxtViewAgent.getText().toString() + ", agent Id value: " + mAgentId );
        });

        dateLiveData.observe(this, date -> {
            mDateAvailable = date;
            mTxtViewDateAvailable.setText(Utils.formatDateToString(mDateAvailable));
            Log.d(TAG, "LiveData date value: " + mDateAvailable + " LiveData TextView date value: "+ mTxtViewDateAvailable.getText().toString());
        });

        pointOfInterestLiveData.observe(this, strings -> {
            mPointsOfInterestList = strings;
            Log.d(TAG, "point of Interest list " + mPointsOfInterestList.size());
        } );

        currencyLiveData.observe(this, s -> {
           // mC
        });
    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);
    }

    private void configureViews() {
        mScrollView = findViewById(R.id.add_property_activity_scrollView);

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
        mTxtViewCurrency = findViewById(R.id.add_property_activity_currency);

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
                Snackbar.make(mScrollView,"Missing values", Snackbar.LENGTH_SHORT).show();
            }
            else {
                mAddPropertyButton.setEnabled(false); // avoid more than one click
                Property propertyAdded = new Property(mAgentId, mCity, mType, mAddress, Integer.parseInt(mPrice), mTxtViewCurrency.getText().toString() ,Integer.parseInt(mSurface), Integer.parseInt(mNbrOfRoom), Integer.parseInt(mNbrOfBedroom),
                        Integer.parseInt(mNbrOfBathroom), mDescription, mDateAvailable, mDateSold, mAgentNameSurname, mPointsOfInterestList,mImagePathList.get(0), isAvailable);
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
                        Image image = new Image(propertyId, mImagePathList.get(i), mImageTitleList.get(i));
                        mPropertyActivityViewModel.createImage(image);
                        Log.d(TAG,"image path list content: " + mImagePathList.get(i) + " " + propertyId);
                    }
                });

                Log.d(TAG, "Image list content: " + mImagePathList);
                finish();
                Snackbar.make(mScrollView,"Property Added", Snackbar.LENGTH_LONG).show();;
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
                            Bitmap resizedBitmap = resizeBitmapForLinearLayout(originalBitmap);

                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                            File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"JPEG_" + timeStamp + "_");

                            createBitmapAtFilePath(selectedImgFile, originalBitmap); // create bitmap at file path for internal copy

                            mBitmapList.add(resizedBitmap);
                            mPropertyActivityViewModel.getBitmapList().setValue(mBitmapList);

                            mImageTitleList.add("Add Title");
                            mPropertyActivityViewModel.getImageTitleList().setValue(mImageTitleList);

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

                        mImageTitleList.add("Add Title");
                        mPropertyActivityViewModel.getImageTitleList().setValue(mImageTitleList);
                        Log.d(TAG, "Selected picture path:" + mChosenPhotoPath);

                        Snackbar.make(mScrollView,"Picture Selected", Snackbar.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e(TAG,"Exception photo choose: "+ e);
                }

            } else{
                Snackbar.make(mScrollView,"Picture has not been selected", Snackbar.LENGTH_SHORT).show();
            }
        }

        if(requestCode == RC_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                try {
                    setPictureFromPath(mTakenPhotoPath, mBitmapList, mImagePathList);
                    mImageTitleList.add("Add Title");
                    mPropertyActivityViewModel.getImageTitleList().setValue(mImageTitleList);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Snackbar.make(mScrollView,"Picture captured", Snackbar.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED){
                Snackbar.make(mScrollView,"Picture not captured", Snackbar.LENGTH_SHORT).show();
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

    // listener after change photo title
    @Override
    public void onConfirmClick(List<String> imageTitleList) {
        mImageTitleList = imageTitleList;
        mPropertyActivityViewModel.getImageTitleList().setValue(imageTitleList);
    }
}
