package com.openclassrooms.realestatemanager.controllers.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.REMViewModel;

import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SearchPropertyFragment extends Fragment {

    // Callback
    public interface OnClickFilterPropertyList{
        void onClickFilterPropertiesListener(List<Property> propertyList);
    }

    private OnClickFilterPropertyList mCallback;

    // FOR DATA
    private REMViewModel mViewModel;
    private static final String TAG = "SearchPropertyFragment";
    private List<Property> mPropertyListFiltered = new ArrayList<>();
    private List<String> mPOIListFilter;
    private int surfaceMin, surfaceMax, priceMin, priceMax, nbrRoomMin, nbrRoomMax, numberOfPicturesMin, numberOfPicturesMax;
    private String type, city;
    private Date dateAvailableMin, dateAvailableMax, dateSoldMin, dateSoldMax;
    private boolean isAvailable;

    // FOR UI
    private Button mFilterPropertyListBtn, mReInitiateFilterButton;

    private EditText mEdtTxtPriceMin, mEdtTxtPriceMax, mEdtTxtSurfaceMin, mEdtTxtSurfaceMax,
    mEdtTxtRoomMin, mEdtTxtRoomMax, mEdtTxtPicturesMin, mEdtTxtCity;
    private Spinner mSpinnerType, mSpinnerStatus;
    private ImageButton mImgBtnDateAvailableMin, mImgBtnDateAvailableMax, mImgBtnDateSoldMin, mImgBtnDateSoldMax;
    private TextView mTxtViewDateAvailableMin, mTxtViewDateAvailableMax,
            mTxtViewDateSoldMin, mTxtViewDateSoldMax, mTxtViewTitleSoldSince, mTxtViewTitleSoldBefore;

    private CheckBox mCckBoxSchool, mCckBoxHospital, mCckBoxRestaurant, mCckBoxMall, mCckBoxCinema, mCckBoxPark;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnClickFilterPropertyList) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement SearchPropertyFragment.OnClickFilterPropertiesListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_property, container, false);

        try {
            initiateFilters();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        configureViewModel();
        configureLiveData();
        configureViews(view);
        configureSpinners();


        onSelectedStatusSpinner();

        onClickDateAvailablePicker(mImgBtnDateAvailableMin, view);
        onClickDateAvailablePicker(mImgBtnDateAvailableMax, view);
        onClickDateAvailablePicker(mImgBtnDateSoldMin, view);
        onClickDateAvailablePicker(mImgBtnDateSoldMax, view);

        onClickFilterPropertyList();
        onClickReInitiateFilter();

        return view;
    }


    // ------ CONFIGURATION METHODS ------

    private void initiateFilters() throws ParseException {
        isAvailable = true;
        surfaceMin = 0;
        surfaceMax = 99999;
        priceMin = 0;
        priceMax = 999999999;
        nbrRoomMin = 0;
        nbrRoomMax = 99;
        type = " ";
        dateAvailableMin = Utils.formatStringToDate("01/01/1900");
        dateAvailableMax = Utils.formatStringToDate("31/12/2100");
        dateSoldMin = Utils.formatStringToDate("01/01/1900");
        dateSoldMax = Utils.formatStringToDate("31/12/2100");
        mPOIListFilter = new ArrayList<>();
        numberOfPicturesMin = 0;
        numberOfPicturesMax = 7;
        city = "";
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }

    private void configureViews(View view) {
        mSpinnerType = view.findViewById(R.id.spinner_type_activity_search);
        mSpinnerStatus = view.findViewById(R.id.spinner_status_activity_search);

        mEdtTxtPriceMin = view.findViewById(R.id.editText_activity_search_minimum_price);
        mEdtTxtPriceMax = view.findViewById(R.id.editText_activity_search_maximum_price);
        mEdtTxtSurfaceMin = view.findViewById(R.id.editTextNumber_activity_search_minimum_surface);
        mEdtTxtSurfaceMax = view.findViewById(R.id.editTextNumber_activity_search_maximum_surface);
        mEdtTxtRoomMin = view.findViewById(R.id.editTextNumber_activity_search_minimum_room);
        mEdtTxtRoomMax = view.findViewById(R.id.editTextNumber_activity_search_maximum_room);
        mEdtTxtPicturesMin = view.findViewById(R.id.editTextNumber_activity_search_number_pictures_minimum);
        mEdtTxtCity = view.findViewById(R.id.editText_activity_search_city);
        mImgBtnDateAvailableMin = view.findViewById(R.id.imageButton_activity_search_date_available_since);
        mImgBtnDateAvailableMax = view.findViewById(R.id.imageButton_activity_search_date_available_before);
        mImgBtnDateSoldMin = view.findViewById(R.id.imageButton_fragment_search_date_sold_since);
        mImgBtnDateSoldMax = view.findViewById(R.id.imageButton_fragment_search_date_sold_before);

        mTxtViewDateAvailableMin = view.findViewById(R.id.textView_date_available_since);
        mTxtViewDateAvailableMax = view.findViewById(R.id.textView_date_available_before);
        mTxtViewDateSoldMin = view.findViewById(R.id.textView_date_sold_since);
        mTxtViewDateSoldMax = view.findViewById(R.id.textView_date_sold_before);
        mTxtViewTitleSoldSince = view.findViewById(R.id.textView_title_sold_since_search_fragment);
        mTxtViewTitleSoldBefore = view.findViewById(R.id.textView_title_sold_before_search_fragment);

        mFilterPropertyListBtn = view.findViewById(R.id.search_property_activity_button);
        mReInitiateFilterButton = view.findViewById(R.id.search_property_activity_button_reinitiate_filters);

        mCckBoxSchool = view.findViewById(R.id.checkBox_school_search_fragment);
        mCckBoxHospital = view.findViewById(R.id.checkBox_hospital_search_fragment);
        mCckBoxRestaurant = view.findViewById(R.id.checkBox_restaurant_search_fragment);
        mCckBoxMall = view.findViewById(R.id.checkBox_mall_search_fragment);
        mCckBoxCinema = view.findViewById(R.id.checkBox_cinema_search_fragment);
        mCckBoxPark = view.findViewById(R.id.checkBox_park_search_fragment);

    }

    private void configureSpinners() {
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(requireContext(),
                R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(adapterType);

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(requireContext(),
                R.array.property_activity_array_status,
                android.R.layout.simple_spinner_item);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStatus.setAdapter(adapterStatus);

    }

    private void configureLiveData() {
        LiveData<Date> dateAvailableMinLiveData = mViewModel.getDateAvailableMin();
        LiveData<Date> dateAvailableMaxLiveData = mViewModel.getDateAvailableMax();
        LiveData<Date> dateSoldMinLiveData = mViewModel.getDateSoldMin();
        LiveData<Date> dateSoldMaxLiveData = mViewModel.getDateSoldMax();
        LiveData<List<Property>> filteredPropertyListLiveData = mViewModel.getFilteredPropertyList();
        LiveData<List<String>> pointOfInterestsLiveData = mViewModel.getPointsOfInterestList();

        filteredPropertyListLiveData.observe(this, properties -> mPropertyListFiltered = properties);

        dateAvailableMinLiveData.observe(this, date -> {
            dateAvailableMin = date;
            mTxtViewDateAvailableMin.setText(Utils.formatDateToString(dateAvailableMin));
        });

        dateAvailableMaxLiveData.observe(this, date -> {
            dateAvailableMax = date;
            mTxtViewDateAvailableMax.setText(Utils.formatDateToString(dateAvailableMax));
        });

        dateSoldMinLiveData.observe(this, date -> {
            dateSoldMin = date;
            mTxtViewDateSoldMin.setText(Utils.formatDateToString(dateSoldMin));
        });

        dateSoldMaxLiveData.observe(this, date -> {
            dateSoldMax = date;
            mTxtViewDateSoldMax.setText(Utils.formatDateToString(dateSoldMax));
        });

        pointOfInterestsLiveData.observe(this, strings -> {
            mPOIListFilter = strings;
            Log.d(TAG, "Size of Point of Interest list " + mPOIListFilter.size());
        });
    }

    // ------ LISTENER ------

    private void onSelectedStatusSpinner() {
        mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mTxtViewTitleSoldBefore.setVisibility(View.GONE);
                        mTxtViewTitleSoldSince.setVisibility(View.GONE);
                        mImgBtnDateSoldMin.setVisibility(View.GONE);
                        mImgBtnDateSoldMax.setVisibility(View.GONE);
                        mTxtViewDateSoldMin.setVisibility(View.GONE);
                        mTxtViewDateSoldMax.setVisibility(View.GONE);
                        break;
                    case 1:
                        mTxtViewTitleSoldBefore.setVisibility(View.VISIBLE);
                        mTxtViewTitleSoldSince.setVisibility(View.VISIBLE);
                        mImgBtnDateSoldMin.setVisibility(View.VISIBLE);
                        mImgBtnDateSoldMax.setVisibility(View.VISIBLE);
                        mTxtViewDateSoldMin.setVisibility(View.VISIBLE);
                        mTxtViewDateSoldMax.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getFilterValueEntered() throws ParseException {
        type = mSpinnerType.getSelectedItem().toString();
        city = mEdtTxtCity.getText().toString().toLowerCase().trim();
        isAvailable = mSpinnerStatus.getSelectedItem().toString().equals(Objects.requireNonNull(getContext()).getString(R.string.available));

        // ternary expression
        surfaceMin = mEdtTxtSurfaceMin.getText().toString().isEmpty()? 0 : Integer.parseInt(mEdtTxtSurfaceMin.getText().toString());
        surfaceMax = mEdtTxtSurfaceMax.getText().toString().isEmpty()? 99999 : Integer.parseInt(mEdtTxtSurfaceMax.getText().toString());
        priceMin = mEdtTxtPriceMin.getText().toString().isEmpty()? 0 : Integer.parseInt(mEdtTxtPriceMin.getText().toString());
        priceMax = mEdtTxtPriceMax.getText().toString().isEmpty()? 999999999: Integer.parseInt(mEdtTxtPriceMax.getText().toString());
        nbrRoomMin = mEdtTxtRoomMin.getText().toString().isEmpty()? 0: Integer.parseInt(mEdtTxtRoomMin.getText().toString());
        nbrRoomMax = mEdtTxtRoomMax.getText().toString().isEmpty()? 99: Integer.parseInt(mEdtTxtRoomMax.getText().toString());
        numberOfPicturesMin = mEdtTxtPicturesMin.getText().toString().isEmpty()? 1: Integer.parseInt(mEdtTxtPicturesMin.getText().toString());

        dateAvailableMin = mTxtViewDateAvailableMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("01/01/1900") : Utils.formatStringToDate(mTxtViewDateAvailableMin.getText().toString());

        dateAvailableMax = mTxtViewDateAvailableMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("31/12/2100") : Utils.formatStringToDate(mTxtViewDateAvailableMax.getText().toString());

        dateSoldMin = mTxtViewDateSoldMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("01/01/1900"): Utils.formatStringToDate(mTxtViewDateSoldMin.getText().toString());

        dateSoldMax = mTxtViewDateSoldMax.getText().toString().isEmpty()?
                Utils.formatStringToDate("31/12/2100"): Utils.formatStringToDate(mTxtViewDateSoldMax.getText().toString());


        if (mCckBoxSchool.isChecked())
            mPOIListFilter.add(mCckBoxSchool.getText().toString());

        if (mCckBoxHospital.isChecked())
            mPOIListFilter.add(mCckBoxHospital.getText().toString());

        if (mCckBoxRestaurant.isChecked())
            mPOIListFilter.add(mCckBoxRestaurant.getText().toString());

        if (mCckBoxMall.isChecked())
            mPOIListFilter.add(mCckBoxMall.getText().toString());

        if (mCckBoxCinema.isChecked())
            mPOIListFilter.add(mCckBoxCinema.getText().toString());

        if (mCckBoxPark.isChecked())
            mPOIListFilter.add(mCckBoxPark.getText().toString());

        Log.d(TAG, "Filtered property type string value :" + type);
    }

    private void onClickFilterPropertyList() {
        mFilterPropertyListBtn.setOnClickListener(view -> {
            try {
                getFilterValueEntered();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mViewModel.filterPropertyListAllType(surfaceMin,surfaceMax ,priceMin ,priceMax
                    ,nbrRoomMin ,nbrRoomMax ,dateAvailableMin ,dateAvailableMax ,dateSoldMin ,dateSoldMax, isAvailable
                    ,numberOfPicturesMin ,numberOfPicturesMax, city)
                    .observe(requireActivity(), properties ->{
                        mPropertyListFiltered.clear();
                        if (type.trim().isEmpty() && mPOIListFilter.isEmpty()){ // if user do not fill type filter, we return the list returned by the query
                            mPropertyListFiltered = properties;
                        }
                        else if (type.trim().isEmpty() && !mPOIListFilter.isEmpty()){
                            mPropertyListFiltered.clear();
                            for (int i = 0; i <properties.size() ; i++) {
                                if (mPOIListFilter.size() == properties.get(i).getPointsOfInterest().size()){
                                    if (Utils.areSameListNoMatterOrder(mPOIListFilter, properties.get(i).getPointsOfInterest())){
                                        mPropertyListFiltered.add(properties.get(i));
                                    }
                                }
                                else if (mPOIListFilter.size() < properties.get(i).getPointsOfInterest().size()){
                                    if (properties.get(i).getPointsOfInterest().containsAll(mPOIListFilter))
                                        mPropertyListFiltered.add(properties.get(i));
                                }
                            }
                        }
                        else if (!type.trim().isEmpty()){
                            mPropertyListFiltered.clear();
                            for (int i = 0; i < properties.size() ; i++) {
                                if (properties.get(i).getType().equals(type) && mPOIListFilter.isEmpty()){
                                    mPropertyListFiltered.add(properties.get(i));
                                }
                                else if (properties.get(i).getType().equals(type) && !mPOIListFilter.isEmpty()){
                                    if (mPOIListFilter.size() == properties.get(i).getPointsOfInterest().size()){
                                        if (Utils.areSameListNoMatterOrder(mPOIListFilter, properties.get(i).getPointsOfInterest())){
                                            mPropertyListFiltered.add(properties.get(i));
                                        }
                                    }
                                    else if (mPOIListFilter.size() < properties.get(i).getPointsOfInterest().size()){
                                        if (properties.get(i).getPointsOfInterest().containsAll(mPOIListFilter))
                                            mPropertyListFiltered.add(properties.get(i));
                                    }
                                }
                            }
                        }

                        mPOIListFilter.clear();
                        Log.d(TAG, "Size POI after CLick :" + mPOIListFilter.size());
                        mViewModel.getPointsOfInterestList().setValue(mPOIListFilter);
                        mViewModel.getFilteredPropertyList().setValue(mPropertyListFiltered);
                        // pass list to main activity then to property list fragment filtered
                        mCallback.onClickFilterPropertiesListener(mPropertyListFiltered);
                        Log.d(TAG, "Filtered property size after type input :" + mPropertyListFiltered.size());
                    });
        });
    }

    private void onClickReInitiateFilter() {
        mReInitiateFilterButton.setOnClickListener(view -> {
            try {
                initiateFilters();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // clear all editText and textViews
            mSpinnerType.setSelection(0);
            mSpinnerStatus.setSelection(0);
            mEdtTxtSurfaceMin.getText().clear();
            mEdtTxtSurfaceMax.getText().clear();
            mEdtTxtPriceMin.getText().clear();
            mEdtTxtPriceMax.getText().clear();
            mEdtTxtRoomMin.getText().clear();
            mEdtTxtRoomMax.getText().clear();
            mTxtViewDateAvailableMin.setText("");
            mTxtViewDateAvailableMax.setText("");
            mTxtViewDateSoldMin.setText("");
            mTxtViewDateSoldMax.setText("");

            mCckBoxSchool.setChecked(false);
            mCckBoxHospital.setChecked(false);
            mCckBoxRestaurant.setChecked(false);
            mCckBoxMall.setChecked(false);
            mCckBoxCinema.setChecked(false);
            mCckBoxPark.setChecked(false);
            mPOIListFilter.clear();

        });
    }


    void onClickDateAvailablePicker(ImageButton imageButton, View view){
        imageButton.setOnClickListener(view2 -> {
            int currentYear, currentMonth, currentDayOfMonth;
            Calendar calendar = Calendar.getInstance();
            currentYear = calendar.get(Calendar.YEAR);
            currentMonth = calendar.get(Calendar.MONTH);
            currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (datePicker, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_available_since)) {
                    mViewModel.getDateAvailableMin().setValue(calendar.getTime());
                }
                else if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_available_before)) {
                    mViewModel.getDateAvailableMax().setValue(calendar.getTime());
                }
                else if (imageButton == view.findViewById(R.id.imageButton_fragment_search_date_sold_since)) {
                    mViewModel.getDateSoldMin().setValue(calendar.getTime());
                }
                else if (imageButton == view.findViewById(R.id.imageButton_fragment_search_date_sold_before)) {
                    mViewModel.getDateSoldMax().setValue(calendar.getTime());
                }
            }, currentYear, currentMonth, currentDayOfMonth);
            datePickerDialog.show();
        });
    }
}
