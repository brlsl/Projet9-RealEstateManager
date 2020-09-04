package com.openclassrooms.realestatemanager.controllers.activities;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.AddAgentBottomSheetFragment;


import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
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


public class AddPropertyActivity extends BasePropertyActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener {


    // FOR DATA
    private BasePropertyActivityViewModel mAddPropertyViewModel;
    private static final String TAG = "AddPropertyActivity";
    private List<Bitmap> mBitmapList = new ArrayList<>();
    private List<String> mImagePathList = new ArrayList<>();

    // ----- FOR UI -----
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mPrice, mAddress, mCity, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface, mEdtTxtNbrRoom, mEdtTxtNbrBedroom, mEdtTxtNbrBathroom, mEdtTxtDescription;
    private ImageButton mImgBtnChoosePicture, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailabilityDate;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable;
    private Date mDateAvailable;

    private long mAgentId;
    private boolean isAvailable = true;
    private String mAgentNameSurname;

    private LinearLayout mLinearLayout;


    // ----- LIFECYCLE -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);


        configureViewModel();

        configureLiveData();
        configureSpinnerType();

        configureViews();


        onClickAddProperty();

        onClickChooseAgent(mImgBtnChooseAgent);
        onClickTakePicture(mImgBtnTakePhoto);
        onClickChoosePicture(mImgBtnChoosePicture);
        onClickDatePicker();

    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mAddPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(BasePropertyActivityViewModel.class);
    }

    // ----- CONFIGURATION METHODS -----

    private void configureLiveData() {
        LiveData<Date> dateLiveData = mAddPropertyViewModel.getDateSelected();
        LiveData<List<Bitmap>> bitmapLiveData = mAddPropertyViewModel.getBitmapList();
        LiveData<Agent> agentLiveData = mAddPropertyViewModel.getAgent();
        LiveData<List<String>> pathListLiveData = mAddPropertyViewModel.getPathList();

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
                mLinearLayout.addView(imageView);
                Log.e(TAG, "Number in bitmap List: " + mBitmapList.size() );
                Log.e(TAG, "Number of photo in linearlayout: " + mLinearLayout.getChildCount() );
            }
        });

        pathListLiveData.observe(this, strings -> {
            mImagePathList = strings;
            Log.e(TAG, "Number in path List data: " + mImagePathList.size() );
        });
    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.property_activity_spinner_type,
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
        mImgBtnAvailabilityDate = findViewById(R.id.button_availability_date_add_activity);

        mTxtViewAgent = findViewById(R.id.agent_chosen_add_property_activity);
        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_add_activity);
        mTxtViewDateAvailable = findViewById(R.id.textView_availability_date_add_property_activity);
        mLinearLayout = findViewById(R.id.linear_layout_photo_add_activity);

        descriptionTitleLengthListener(mEdtTxtDescription, mTxtViewDescriptionTitle);

    }

    private void onClickDatePicker(){
        mImgBtnAvailabilityDate.setOnClickListener(view -> {
            int currentYear, currentMonth, currentDayOfMonth ;
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mAddPropertyViewModel.getDateSelected().setValue(calendar.getTime());
                }
            }, currentYear, currentMonth, currentDayOfMonth);
            datePickerDialog.show();
        });
    }


    // ----- ON CLICK LISTENER -----

    private void onClickAddProperty() {
        mAddPropertyButton.setOnClickListener(view -> {
            if (!isPossibleToAddProperty())
            {
                Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
            }
            else {
                Property propertyAdded = new Property(mAgentId, mCity, mType, mAddress, mPrice, mSurface, mNbrOfRoom, mNbrOfBedroom,
                        mNbrOfBathroom, mDescription, mDateAvailable, mAgentNameSurname, mImagePathList.get(0), isAvailable);
                mAddPropertyViewModel.createProperty(propertyAdded);

                // wait between property creation and get all properties from database
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mAddPropertyViewModel.getPropertyList().observe(this, properties -> {
                    long id = properties.get(properties.size() -1).getId();

                    // write image path in DB
                    for (int i = 0; i < mImagePathList.size() ; i++) {
                        Image image = new Image(id, mImagePathList.get(i));
                        mAddPropertyViewModel.createImage(image);
                        Log.e(TAG,"contenu de la liste: " + mImagePathList.get(i) + " " + id);

                    }
                });

                Log.e(TAG, "Contenu de Image List: " + mImagePathList);
                finish();
                Toast.makeText(AddPropertyActivity.this, "Property added date" + mDateAvailable +" et" + mAgentNameSurname, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ------- LISTENER ------

    @Override
    public void onClickAgentItem(Agent agent) {
        mAddPropertyViewModel.getAgent().setValue(agent);
    }

    // ----- ON ACTIVITY RESULT -----

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK && data != null && data.getData() != null){
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),data.getData());
                            Bitmap originalBitmap = ImageDecoder.decodeBitmap(source);

                            //resize original bitmap for linear layout
                            float aspectRatio = originalBitmap.getWidth() / (float) originalBitmap.getHeight();
                            int height = mLinearLayout.getHeight();
                            int width = Math.round(height * aspectRatio);

                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height,false);


                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), timeStamp + imageFileName);

                            createFileFromBitmap(selectedImgFile, originalBitmap);

                            mBitmapList.add(resizedBitmap);
                            mAddPropertyViewModel.getBitmapList().setValue(mBitmapList);

                            Log.e(TAG, "Bitmap List after add :" +mBitmapList.size() + mBitmapList);

                            mImagePathList.add(selectedImgFile.getAbsolutePath());
                            mAddPropertyViewModel.getPathList().setValue(mImagePathList);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                    {
                        Uri uri = data.getData();
                        selectPhotoPath = getRealPathFromUri(uri);
                        setPictureFromPath(selectPhotoPath); // and add to linear layout

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

                try {
                    setPictureFromPath(takePhotoPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();



            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPictureFromPath(String photoPath) throws IOException {
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

        // create an internal bitmap copy in case picture is moved or deleted
        Bitmap bitmapCopy = Bitmap.createBitmap(bitmap);
        File selectedImgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "test");

        createFileFromBitmap(selectedImgFile, bitmapCopy);

        mBitmapList.add(bitmap);
        mAddPropertyViewModel.getBitmapList().setValue(mBitmapList);

        Log.e(TAG, "Bitmap List after add :" +mBitmapList.size() + mBitmapList);


        mImagePathList.add(selectedImgFile.getAbsolutePath());
        mAddPropertyViewModel.getPathList().setValue(mImagePathList);

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

        if (mTxtViewAgent.getText().toString().trim().isEmpty() || mType.isEmpty() || mPrice.isEmpty() || mAddress.isEmpty() || mCity.isEmpty() ||
                mSurface.isEmpty() || mNbrOfRoom.isEmpty() || mNbrOfBedroom.isEmpty() || mNbrOfBathroom.isEmpty() || mDescription.isEmpty() ||
                mBitmapList.size() == 0 || mTxtViewAgent.getText().length() == 0 || mDateAvailable == null) {
            return false;
        }
        else return true;

    }
}
