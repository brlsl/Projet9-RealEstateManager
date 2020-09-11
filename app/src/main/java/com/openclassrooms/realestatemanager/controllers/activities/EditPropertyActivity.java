package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;

public class EditPropertyActivity extends BasePropertyActivity {

    private Date mDateAvailable, mDateSold;
    private ArrayAdapter<CharSequence> mTypeArrayAdapter;
    private static final String TAG = "EditPropertyActivity";
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private List<String> mImagePathList = new ArrayList<>(), mPointsOfInterestList = new ArrayList<>();

    private long mAgentId, mPropertyId;
    private boolean isAvailable = true;
    private String  mType, mPrice, mAddress, mCity, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private String mAgentNameSurname;

    // FOR UI

    private EditText mEdtTxtPrice, mEdtTxtAddress, mEdtTxtCity, mEdtTxtSurface, mEdtTxtNbrRoom, mEdtTxtNbrBedroom,
            mEdtTxtNbrBathroom, mEdtTxtDescription;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewAvailableDate, mTxtViewSoldDate, mTxtViewChooseDateTitle;
    private Spinner mTypeSpinner, mIsAvailableSpinner;

    private LinearLayout mLinearLayout;

    private ImageButton mImgBtnChoosePicture, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailableDate, mImgBtnSoldDate;
    private CheckBox mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant, mCckBoxMall, mCckBoxCinema, mCckBoxPark;
    private Button mEditPropertyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        configureViews();
        configureLiveData();
        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);
        configureSpinnerType();
        configureSpinnerPropertyStatus();

        onClickCheckBoxes(mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant,mCckBoxMall, mCckBoxCinema,
                mCckBoxPark, mPointsOfInterestList, mPropertyActivityViewModel);
        onClickTakePicture();
        onClickChoosePicture();
        onClickChooseAgent(mImgBtnChooseAgent);
        onClickAvailableDatePicker(mImgBtnAvailableDate, mPropertyActivityViewModel);
        onClickSoldDatePicker();

        onSelectedStatusPropertySpinner();

        onClickEditProperty();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            long propertyId = bundle.getLong(PROPERTY_ID_KEY);
            long agentId = bundle.getLong(PROPERTY_AGENT_ID_KEY);

            mPropertyId = propertyId;

            System.out.println("EditActivity propertyId: " + propertyId + " + agentId :" + agentId);

            if (savedInstanceState == null){ //get data from database once and if screen rotation, don't fetch data again
                // FOR DATA
                LiveData<Agent> liveDataAgent = mPropertyActivityViewModel.getAgent(agentId);

                liveDataAgent.observe(this, agent -> {
                    mPropertyActivityViewModel.getAgentMutableLiveData().setValue(agent);
                    mAgentNameSurname = agent.getName() + " " + agent.getSurname();
                    mTxtViewAgent.setText(mAgentNameSurname);
                });

                LiveData<Property> liveDataProperty = mPropertyActivityViewModel.getProperty(propertyId, agentId);
                liveDataProperty.observe(this, property -> {

                    for (int i = 0; i < mTypeArrayAdapter.getCount() ; i++) {
                        if (Objects.requireNonNull(mTypeArrayAdapter.getItem(i)).toString().equals(property.getType())){
                            mTypeSpinner.setSelection(i);
                        }
                    }

                    List <String> poiList = property.getPointsOfInterest();
                    if (!poiList.isEmpty()) {
                        if (poiList.contains(mCckBoxSchool.getText().toString())) {
                            mCckBoxSchool.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxSchool.getText().toString());
                        }
                        if (poiList.contains(mCckBoxHospital.getText().toString())) {
                            mCckBoxHospital.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxHospital.getText().toString());
                        }
                        if (poiList.contains(mCckBoxRestaurant.getText().toString())) {
                            mCckBoxRestaurant.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxRestaurant.getText().toString());
                        }
                        if (poiList.contains(mCckBoxMall.getText().toString())) {
                            mCckBoxMall.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxMall.getText().toString());
                        }
                        if (poiList.contains(mCckBoxCinema.getText().toString())) {
                            mCckBoxCinema.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxCinema.getText().toString());
                        }
                        if (poiList.contains(mCckBoxPark.getText().toString())) {
                            mCckBoxPark.setChecked(true);
                            mPointsOfInterestList.add(mCckBoxPark.getText().toString());
                        }
                        mPropertyActivityViewModel.getPointsOfInterestList().setValue(mPointsOfInterestList);
                    }


                    if (property.isAvailable()){
                        mIsAvailableSpinner.setSelection(0); // Available
                        mTxtViewSoldDate.setVisibility(View.GONE);
                        mTxtViewChooseDateTitle.setVisibility(View.GONE);
                        mImgBtnSoldDate.setVisibility(View.GONE);

                    } else {
                        mIsAvailableSpinner.setSelection(1); // Sold
                        mTxtViewSoldDate.setVisibility(View.VISIBLE);
                        mTxtViewChooseDateTitle.setVisibility(View.VISIBLE);
                        mImgBtnSoldDate.setVisibility(View.VISIBLE);
                    }

                    mEdtTxtPrice.setText(property.getPrice());
                    mEdtTxtAddress.setText(property.getAddress());
                    mEdtTxtCity.setText(property.getCity());
                    mEdtTxtSurface.setText(property.getSurface());
                    mEdtTxtNbrRoom.setText(property.getNumberOfRooms());
                    mEdtTxtNbrBedroom.setText(property.getNumberOfBedrooms());
                    mEdtTxtNbrBathroom.setText(property.getNumberOfBathRooms());
                    mEdtTxtDescription.setText(property.getDescription());

                    mPropertyActivityViewModel.getDateAvailable().setValue(property.getDateAvailable());

                    //mTxtViewDateAvailable.setText(Utils.formatDateToString(property.getDateAvailable()));
                    //mTxtViewAgent.setText(property.getAgentNameSurname());


                    System.out.println("TEST onCreate :" + property.getDateAvailable().toString());
                });

                LiveData<List<Image>> liveDataImageList = mPropertyActivityViewModel.getImageListOneProperty(propertyId);
                liveDataImageList.observe(this, imageList -> {
                    for (int i = 0; i <imageList.size() ; i++) {
                        try {
                            setPictureFromPath(imageList.get(i).getImagePath(), mBitmapList, mImagePathList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void onClickSoldDatePicker(){
            mImgBtnSoldDate.setOnClickListener(view -> {
                int currentYear, currentMonth, currentDayOfMonth ;
                Calendar calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mPropertyActivityViewModel.getDateSold().setValue(calendar.getTime());
                }, currentYear, currentMonth, currentDayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(mDateAvailable.getTime());
                datePickerDialog.show();

        });
    }

    private void onSelectedStatusPropertySpinner() {
        mIsAvailableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mTxtViewSoldDate.setVisibility(View.GONE);
                        mTxtViewChooseDateTitle.setVisibility(View.GONE);
                        mImgBtnSoldDate.setVisibility(View.GONE);
                        break;

                    case 1:
                        mTxtViewSoldDate.setVisibility(View.VISIBLE);
                        mTxtViewChooseDateTitle.setVisibility(View.VISIBLE);
                        mImgBtnSoldDate.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    // ------- LISTENER ------

    @Override
    public void onClickAgentItem(Agent agent) {
        mPropertyActivityViewModel.getAgentMutableLiveData().setValue(agent);
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


    private void onClickEditProperty() {
        mEditPropertyButton.setOnClickListener(view -> {
            if (!isPossibleToEditProperty()){
                Toast.makeText(EditPropertyActivity.this, "Missing a value", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(EditPropertyActivity.this, "Property Edited", Toast.LENGTH_SHORT).show();

                Property updatedProperty = new Property(mAgentId, mCity, mType, mAddress, mPrice, mSurface, mNbrOfRoom, mNbrOfBedroom,
                        mNbrOfBathroom, mDescription, mDateAvailable, mDateSold, mAgentNameSurname, mPointsOfInterestList,
                        mImagePathList.get(0), isAvailable);

                updatedProperty.setId(mPropertyId);

                mPropertyActivityViewModel.updateProperty(updatedProperty);

                mPropertyActivityViewModel.deleteImagesOneProperty(mPropertyId);
                for (int i = 0; i <mImagePathList.size() ; i++) {
                    Image image = new Image(mPropertyId, mImagePathList.get(i));
                    mPropertyActivityViewModel.createImage(image);
                }
                finish();
            }

        });


    }


    @SuppressLint("ResourceType")
    private void configureLiveData() {
        LiveData<Date> dateAvailableLiveData = mPropertyActivityViewModel.getDateAvailable();
        LiveData<Date> dateSoldLiveData = mPropertyActivityViewModel.getDateSold();
        LiveData<Agent> agentLiveData = mPropertyActivityViewModel.getAgentMutableLiveData();
        LiveData<List<Bitmap>> bitmapLiveData = mPropertyActivityViewModel.getBitmapList();
        LiveData<List<String>> pathListLiveData = mPropertyActivityViewModel.getPathList();
        LiveData<List<String>> pointOfInterestLiveData = mPropertyActivityViewModel.getPointsOfInterestList();

        dateAvailableLiveData.observe(this, date -> {
            mDateAvailable = date;
            mTxtViewAvailableDate.setText(Utils.formatDateToString(date));
        });

        dateSoldLiveData.observe(this, date -> {
            mDateSold = date;
            mTxtViewSoldDate.setText(Utils.formatDateToString(date));
            if (mDateAvailable.after(date)){
                mTxtViewSoldDate.setText("");
            }
        });

        agentLiveData.observe(this, agent -> {
            mAgentNameSurname = agent.getName()+" "+agent.getSurname();
            mTxtViewAgent.setText(mAgentNameSurname);
            mAgentId = agent.getId();
        });

        bitmapLiveData.observe(this, bitmapList -> {
            mLinearLayout.removeAllViews();
            for (int i = 0; i < bitmapList.size() ; i++) {
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
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
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
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
        mTypeSpinner = findViewById(R.id.edit_activity_spinner_type);
        mTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
        mTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(mTypeArrayAdapter);

    }

    private void configureSpinnerPropertyStatus() {
        mIsAvailableSpinner = findViewById(R.id.spinner_availability_edit_property_activity);
        ArrayAdapter<CharSequence> propertyStatusArrayAdapter = ArrayAdapter.createFromResource(this, R.array.property_activity_array_is_available,
                android.R.layout.simple_spinner_item);
        propertyStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIsAvailableSpinner.setAdapter(propertyStatusArrayAdapter);
    }

    private void configureViews() {
        mEdtTxtPrice = findViewById(R.id.editText_price_edit_activity);
        mEdtTxtAddress = findViewById(R.id.editText_address_edit_activity);
        mEdtTxtCity = findViewById(R.id.editText_city_edit_activity);
        mEdtTxtSurface = findViewById(R.id.editText_surface_edit_activity);
        mEdtTxtNbrRoom = findViewById(R.id.editText_number_of_room_edit_activity);
        mEdtTxtNbrBedroom = findViewById(R.id.editText_number_of_bedroom_edit_activity);
        mEdtTxtNbrBathroom = findViewById(R.id.editText_number_of_bathroom_edit_activity);
        mEdtTxtDescription = findViewById(R.id.editText_description_edit_activity);

        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_edit_activity);
        mTxtViewAgent = findViewById(R.id.agent_chosen_edit_property_activity);
        mTxtViewAvailableDate = findViewById(R.id.textView_availability_date_edit_property_activity);
        mTxtViewSoldDate = findViewById(R.id.textView_sold_date_edit_property_activity);
        mTxtViewChooseDateTitle = findViewById(R.id.textView_choose_sold_date_edit_activity);

        mImgBtnChoosePicture = findViewById(R.id.image_button_choose_picture_edit_activity);
        mImgBtnTakePhoto = findViewById(R.id.image_button_take_picture_edit_activity);
        mImgBtnChooseAgent = findViewById(R.id.image_button_choose_agent_edit_activity);
        mImgBtnAvailableDate = findViewById(R.id.image_button_availability_date_edit_activity);
        mImgBtnSoldDate = findViewById(R.id.imageButton_sold_date_edit_activity);
        mEditPropertyButton = findViewById(R.id.activity_edit_property_button);

        mCckBoxSchool = findViewById(R.id.checkBox_school_edit_activity);
        mCckBoxHospital = findViewById(R.id.checkBox_hospital_edit_activity);
        mCckBoxRestaurant = findViewById(R.id.checkBox_restaurant_edit_activity);
        mCckBoxMall = findViewById(R.id.checkBox_mall_edit_activity);
        mCckBoxCinema = findViewById(R.id.checkBox_cinema_edit_activity);
        mCckBoxPark = findViewById(R.id.checkBox_park__edit_activity);

        mLinearLayout = findViewById(R.id.linear_layout_photo_edit_activity);

    }

    // ------ LISTENER ------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK && data != null && data.getData() != null){
                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                            Bitmap originalBitmap = ImageDecoder.decodeBitmap(source);

                            //resize original bitmap for linear layout
                            Bitmap resizedBitmap = resizeBitmapForLinearLayout(originalBitmap, mLinearLayout);

                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                            File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"JPEG_" + timeStamp + "_");

                            createBitmapAtFilePath(selectedImgFile, resizedBitmap);

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


    private boolean isDateSaved(){
        switch (mIsAvailableSpinner.getSelectedItemPosition()){
            case 0:
                return mTxtViewAvailableDate.getText().length() != 0;

            case 1:
                return mTxtViewSoldDate.getText().length() != 0;
        }
        return false;
    }

    private boolean isPossibleToEditProperty(){
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
                mBitmapList.size() == 0 || mTxtViewAgent.getText().length() == 0  || !isDateSaved()) {
            return false;
        }
        else return true;
    }

}