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
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.AddAgentBottomSheetFragment;


import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class AddPropertyActivity extends BaseActivity implements AddAgentBottomSheetFragment.OnAgentItemClickListener {

    // ----- FOR DATA -----

    private static final String READ_EXT_STORAGE_PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String CAMERA_PERMS = Manifest.permission.CAMERA;
    private static final int RC_CHOOSE_PHOTO = 100;
    public static final int RC_TAKE_PHOTO = 200;
    private static final String TAG = "AddPropertyActivity";

    // ----- FOR UI -----
    private Button mAddPropertyButton;
    private Spinner mTypeSpinner;
    private String  mType, mCity, mPrice, mAddress, mSurface, mNbrOfRoom, mNbrOfBedroom, mNbrOfBathroom, mDescription;
    private EditText mEdtTxtCity,mEdtTxtPrice, mEdtTxtAddress, mEdtTxtSurface,mEdtTxtNbrRoom,mEdtTxtNbrBedroom,mEdtTxtNbrBathroom, mEdtTxtDescription;
    private ImageButton mImgBtnSelectPhoto, mImgBtnTakePhoto, mImgBtnChooseAgent, mImgBtnAvailabilityDate;
    private TextView mTxtViewAgent, mTxtViewDescriptionTitle, mTxtViewDateAvailable;
    private Date mDateAvailable;

    private long mAgentId;
    private boolean isAvailable = true;
    private String mAgentNameSurname;

    private LinearLayout mLinearLayout;
    private List<Bitmap> mBitmapList = new ArrayList<>();

    private LiveData<Date> mDateSelected;
    private LiveData<List<Bitmap>> bitmapLiveData;
    private LiveData<Agent> mAgent;
    private LiveData<List<String>> mPathListLiveData;

    List<String> mImagePathList = new ArrayList<>();

    // ----- LIFECYCLE -----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);


        configureLiveData();
        configureSpinnerType();

        configureViews();

        onClickChoosePicture();
        onClickAddProperty();
        onClickTakePicture();
        onClickChooseAgent();

    }

    // ----- CONFIGURATION METHODS -----


    private void configureLiveData() {

        mDateSelected = mViewModel.getDateSelected();
        bitmapLiveData = mViewModel.getBitmapList();
        mAgent = mViewModel.getAgent();
        mPathListLiveData = mViewModel.getPathList();


        mAgent.observe(this, agent -> {
            mAgentNameSurname = agent.getName()+" "+agent.getSurname();
            mTxtViewAgent.setText(mAgentNameSurname);
            mAgentId = agent.getId();
            Log.e(TAG, "LiveData name value: " +mAgentNameSurname + ", TextView name value: "+ mTxtViewAgent.getText().toString() + ", agent Id value: " + mAgentId );
        });


        mDateSelected.observe(this, date -> {
            mDateAvailable = date;
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
            mTxtViewDateAvailable.setText(sdf.format(mDateAvailable));
            Log.e(TAG, "LiveData date value: " + mDateAvailable + " LiveData TextView date value: "+ mTxtViewDateAvailable.getText().toString());
        });


        bitmapLiveData.observe(this, bitmapList -> {
            mLinearLayout.removeAllViews();
            for (int i = 0; i < bitmapList.size() ; i++) {
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
                mBitmapList = bitmapList;
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmapList.get(i));
                mLinearLayout.addView(imageView);
                Log.e(TAG, "Number in bitmap List: " + mBitmapList );
                Log.e(TAG, "Number of photo in linearlayout: " + mLinearLayout.getChildCount() );
            }
        });

        mPathListLiveData.observe(this, strings -> {

            mImagePathList = strings;
            Log.e(TAG, "Number in path List data: " + mImagePathList.size() );
        });

    }

    private void configureSpinnerType() {
        mTypeSpinner = findViewById(R.id.add_activity_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.add_property_activity_spinner_type,
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

        mImgBtnSelectPhoto = findViewById(R.id.button_select_picture_add_activity);
        mImgBtnTakePhoto = findViewById(R.id.button_take_picture_add_activity);
        mImgBtnChooseAgent = findViewById(R.id.choose_agent_button_add_activity);
        mImgBtnAvailabilityDate = findViewById(R.id.button_avaibility_date_add_activity);

        mTxtViewAgent = findViewById(R.id.agent_chosen_add_property_activity);
        mTxtViewDescriptionTitle = findViewById(R.id.textview_description_add_activity);
        mTxtViewDateAvailable = findViewById(R.id.textView__avaibility_date_add_property_activity);
        mLinearLayout = findViewById(R.id.linear_layout_photo_add_activity);

        descriptionTitleLengthListener();
        setDatePickerDialog();
    }

    private void setDatePickerDialog(){
        mImgBtnAvailabilityDate.setOnClickListener(view -> {
            int currentYear, currentMonth, currentDayOfMonth ;
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddPropertyActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    mViewModel.getDateSelected().setValue(calendar.getTime());


                }
            }, currentYear, currentMonth, currentDayOfMonth);
            datePickerDialog.show();
        });
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
        mImgBtnChooseAgent.setOnClickListener(view -> {
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


            if (mTxtViewAgent.getText().toString().trim().isEmpty() || mType.isEmpty() || mPrice.isEmpty() || mAddress.isEmpty() || mCity.isEmpty() ||
                    mSurface.isEmpty() || mNbrOfRoom.isEmpty() || mNbrOfBedroom.isEmpty() || mNbrOfBathroom.isEmpty() || mDescription.isEmpty() ||
                    mImagePathList.size() == 0 || mTxtViewAgent.getText().length() == 0 || mDateAvailable == null
            )
            {
                Toast.makeText(AddPropertyActivity.this, "Missing values", Toast.LENGTH_SHORT).show();
            }
            else {
                Property propertyAdded = new Property(mAgentId,
                        mCity,
                        mType,
                        mAddress,
                        mPrice,
                        mSurface,
                        mNbrOfRoom,
                        mNbrOfBedroom,
                        mNbrOfBathroom,
                        mDescription,
                        mDateAvailable,
                        mAgentNameSurname,
                        mImagePathList.get(0),
                        isAvailable);
                mViewModel.createProperty(propertyAdded);

                // wait between property creation and get all properties from database
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mViewModel.getPropertyList().observe(this, properties -> {
                    long id = properties.get(properties.size() -1).getId();

                    // write image path in DB
                    for (int i = 0; i < mImagePathList.size() ; i++) {
                        Image image = new Image(id, mImagePathList.get(i));
                        mViewModel.createImage(image);
                        Log.e(TAG,"contenu de la liste: " + mImagePathList.get(i) + " " + id);

                    }
                });

                Log.e(TAG, "Contenu de Image List: " + mImagePathList);
                finish();
                Toast.makeText(AddPropertyActivity.this, "Property added date" + mDateAvailable +" et" + mAgentNameSurname, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClickAgentItem(Agent agent) {
        mViewModel.getAgent().setValue(agent);
    }


    @AfterPermissionGranted(RC_CHOOSE_PHOTO)
    private void onClickChoosePicture() {
        mImgBtnSelectPhoto.setOnClickListener(view ->{
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
        mImgBtnTakePhoto.setOnClickListener(view ->{
            if(!EasyPermissions.hasPermissions(this, CAMERA_PERMS)){
                EasyPermissions.requestPermissions(this,"Real Estate Manager needs your permission to use camera", RC_TAKE_PHOTO, CAMERA_PERMS);
                return;
            }
            takePictureIntent();
        });

    }

    String takePhotoPath;
    String selectPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        takePhotoPath = image.getAbsolutePath();
        Log.e(TAG, "Photo Path :" + takePhotoPath);

        return image;
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e(TAG, "Create File" + photoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Error while creating the File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.openclassrooms.realestatemanager.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, RC_TAKE_PHOTO);
            }
        }
    }

    // ----- ON ACTIVITY RESULT -----

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CHOOSE_PHOTO){
            if (resultCode == RESULT_OK && data != null){
                try{
                    Uri uri = data.getData();
                    selectPhotoPath = getRealPathFromUri(uri);
                    setPicture(selectPhotoPath); // and add to linear layout



                    Log.e(TAG, "Selected picture path:" + selectPhotoPath);

                    Toast.makeText(this, "Picture Selected", Toast.LENGTH_SHORT).show();
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

                /*
                // TODO: ok jusqu'ici, il faut réduire le bitmap, ajouter dans la view et ajouter en db
                File f = new File(takePhotoPath);
                Uri contentUri = Uri.fromFile(f);
                try {
                    if (Build.VERSION.SDK_INT <= 28) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentUri);
                        new ImageSaver(this)
                                .setFileName("nameTest")
                                .setDirectoryName("directoryTest")
                                .save(bitmap);


                        //ImageUtils.saveToInternalStorage(bitmap, getApplicationContext());
                    } else {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), contentUri);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                        //ImageUtils.saveToInternalStorage(bitmap, getApplicationContext());
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
                */

                setPicture(takePhotoPath);

                Toast.makeText(this, "Picture captured", Toast.LENGTH_SHORT).show();



            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Picture not captured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(takePhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPicture(String photoPath) {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 400;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, bmOptions);
        Log.e(TAG, "Image décodée 1");
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        mBitmapList.add(bitmap);
        mViewModel.getBitmapList().setValue(mBitmapList);

        Log.e(TAG, "Bitmap List after add :" +mBitmapList.size() + mBitmapList);

        Log.e(TAG, "Image décodée 2");
        //ImageView imageView = new ImageView(this);

        //imageView.setImageBitmap(bitmap);

        mImagePathList.add(photoPath);
        mViewModel.getPathList().setValue(mImagePathList);

        Log.e("Tag", "Image set bitmap  ");
        //addPhotoToLinearLayout(imageView);

        Log.e("Tag", "Image décodée ");
    }

    private void addPhotoToLinearLayout(ImageView imageView) {

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(5,5,5,5);


        mLinearLayout.addView(imageView);

        Log.e(TAG, "Number of picture in Linear Layout : " + mLinearLayout.getChildCount());
    }

    //helper to retrieve the path of an image URI
    public String getRealPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor!=null)
        {
            cursor.moveToFirst();
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        }
        else{
            return uri.getPath();
        }
    }

}
