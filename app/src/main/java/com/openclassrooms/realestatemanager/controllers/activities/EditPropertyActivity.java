package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
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
import java.util.Objects;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_AGENT_ID_KEY;
import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_ID_KEY;

public class EditPropertyActivity extends BasePropertyActivity {


    // FOR DATA
    private Property mProperty;
    private LiveData<Property> liveDataProperty;
    private LiveData<List<Image>> liveDataImageList;
    private Date mDateAvailable;
    private ArrayAdapter<CharSequence> mSpinnerArrayAdapter;
    private static final String TAG = "EditPropertyActivity";
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private List<String> mImagePathList = new ArrayList<>();

    // FOR UI

    private EditText mEdtTxtPrice, mEdtTxtAddress, mEdtTxtCity, mEdtTxtSurface, mEdtTxtNbrOfRoom, mEdtTxtNbrOfBedroom,
            mEdtTxtNbrOfBathroom, mEdtTxtDescription;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable;
    private Spinner mTypeSpinner;

    private LinearLayout mLinearLayout;

    private ImageButton mImgBtnChoosePicture, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailabilityDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        configureViews();
        configureLiveData();
        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);
        configureSpinnerType();

        onClickTakePicture(mImgBtnTakePhoto);
        onClickChoosePicture(mImgBtnChoosePicture);
        onClickChooseAgent(mImgBtnChooseAgent);
        onClickDatePicker(mImgBtnAvailabilityDate, mPropertyActivityViewModel);

        
        onClickEditProperty();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            long propertyId = bundle.getLong(PROPERTY_ID_KEY);
            long agentId = bundle.getLong(PROPERTY_AGENT_ID_KEY);

            System.out.println("EditActivity propertyId: " + propertyId + " + agentId :" + agentId);

            liveDataProperty = mPropertyActivityViewModel.getProperty(propertyId, agentId);

            if (savedInstanceState == null){ //get data from database once and if screen rotation, don't fetch data again
                liveDataProperty.observe(this, property -> {

                    for (int i = 0; i < mSpinnerArrayAdapter.getCount() ; i++) {
                        if (Objects.requireNonNull(mSpinnerArrayAdapter.getItem(i)).toString().equals(property.getType())){
                            mTypeSpinner.setSelection(i);
                        }
                    }
                    mEdtTxtPrice.setText(property.getPrice());
                    mEdtTxtAddress.setText(property.getAddress());
                    mEdtTxtCity.setText(property.getCity());
                    mEdtTxtSurface.setText(property.getSurface());
                    mEdtTxtNbrOfRoom.setText(property.getNumberOfRooms());
                    mEdtTxtNbrOfBedroom.setText(property.getNumberOfBedrooms());
                    mEdtTxtNbrOfBathroom.setText(property.getNumberOfBathRooms());
                    mEdtTxtDescription.setText(property.getDescription());

                    mPropertyActivityViewModel.getDateSelected().setValue(property.getDateAvailable());
                    mPropertyActivityViewModel.getAgentNameSurname().setValue(property.getAgentNameSurname());
                    //mTxtViewDateAvailable.setText(Utils.formatDateToString(property.getDateAvailable()));
                    //mTxtViewAgent.setText(property.getAgentNameSurname());

                    mProperty = property;

                    System.out.println("TEST onCreate :" + property.getDateAvailable().toString());
                });

                liveDataImageList = mPropertyActivityViewModel.getImageListOneProperty(propertyId);
                liveDataImageList.observe(this, imageList -> {
                    for (int i = 0; i <imageList.size() ; i++) {
                        setPictureFromPath(imageList.get(i).getImagePath(), mBitmapList, mImagePathList);
                    }

                });
            }

        }

    }

    private void onClickEditProperty() {
    }


    private void configureLiveData() {
        LiveData<Date> dateAvailableLiveData = mPropertyActivityViewModel.getDateSelected();
        LiveData<String> agentLiveData = mPropertyActivityViewModel.getAgentNameSurname();
        LiveData<List<Bitmap>> bitmapLiveData = mPropertyActivityViewModel.getBitmapList();
        LiveData<List<String>> pathListLiveData = mPropertyActivityViewModel.getPathList();

        dateAvailableLiveData.observe(this, date -> {
            mTxtViewDateAvailable.setText(Utils.formatDateToString(date));

        });

        agentLiveData.observe(this, s -> {
            mTxtViewAgent.setText(s);
        });

        bitmapLiveData.observe(this, bitmapList -> {
            mLinearLayout.removeAllViews();
            for (int i = 0; i < bitmapList.size() ; i++) {
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
                mBitmapList = bitmapList;
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmapList.get(i));
                imageView.setPadding(5,20,5,20);
                mLinearLayout.addView(imageView);
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
                Log.e(TAG, "Number of photo in linearlayout: " + mLinearLayout.getChildCount() );
            }
        });

        pathListLiveData.observe(this, strings -> {
            mImagePathList = strings;
            Log.e(TAG, "Number in path List data: " + mImagePathList.size() );
        });

    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.edit_activity_spinner_type);
        mSpinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.property_activity_spinner_type,
                android.R.layout.simple_spinner_item);
        mSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(mSpinnerArrayAdapter);

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
        mTxtViewAgent = findViewById(R.id.agent_chosen_edit_property_activity);
        mTxtViewDateAvailable = findViewById(R.id.textView_availability_date_edit_property_activity);

        mImgBtnChoosePicture = findViewById(R.id.button_choose_picture_edit_activity);
        mImgBtnTakePhoto = findViewById(R.id.button_take_picture_edit_activity);
        mImgBtnChooseAgent = findViewById(R.id.choose_agent_button_edit_activity);
        mImgBtnAvailabilityDate = findViewById(R.id.button_availability_date_edit_activity);

        mLinearLayout = findViewById(R.id.linear_layout_photo_edit_activity);

    }

    // ------ LISTENER ------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK && data != null && data.getData() != null){
                try {

                    if (Build.VERSION.SDK_INT >= 29) {
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                            Bitmap bitmap = ImageDecoder.decodeBitmap(source);

                            //resize bitmap
                            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
                            int height = mLinearLayout.getHeight();
                            int width = Math.round(height * aspectRatio);


                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height,false);


                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), timeStamp + imageFileName);

                            createFileFromBitmap(selectedImgFile, resizedBitmap);
                            //setPicture(selectedImgFile.getAbsolutePath());

                            mBitmapList.add(resizedBitmap);
                            mPropertyActivityViewModel.getBitmapList().setValue(mBitmapList);

                            Log.e(TAG, "Bitmap List after add :" +mBitmapList.size() + mBitmapList);


                            mImagePathList.add(selectedImgFile.getAbsolutePath());
                            mPropertyActivityViewModel.getPathList().setValue(mImagePathList);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                    {
                        Uri uri = data.getData();
                        selectPhotoPath = getRealPathFromUri(uri);
                        setPictureFromPath(selectPhotoPath, mBitmapList, mImagePathList); // and add to linear layout

                        Log.e(TAG, "Selected picture path:" + selectPhotoPath);

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

                setPictureFromPath(takePhotoPath, mBitmapList, mImagePathList);

                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();



            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

   


}