package com.openclassrooms.realestatemanager.controllers.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class SearchPropertyFragment extends BaseFragment {

    // Callback
    public interface OnClickFilterPropertyList{
        void onClickFilterPropertiesListener(List<Property> propertyList);
    }

    private OnClickFilterPropertyList mCallback;

    // FOR DATA
    private REMViewModel mViewModel;
    private static final String TAG = "SearchPropertyFragment";
    private List<Property> mPropertyListFiltered = new ArrayList<>();
    private int surfaceMin, surfaceMax, priceMin, priceMax, nbrRoomMin, nbrRoomMax;
    private String type;
    private Date dateAvailableMin, dateAvailableMax, dateSoldMin, dateSoldMax;

    // FOR UI
    private Button mFilterPropertyListBtn, mReInitiateFilterButton;

    private EditText mEdtTxtPriceMin, mEdtTxtPriceMax, mEdtTxtSurfaceMin, mEdtTxtSurfaceMax, mEdtTxtRoomMin, mEdtTxtRoomMax;
    private Spinner mSpinnerType;
    private ImageButton mImgBtnDateAvailableMin, mImgBtnDateAvailableMax, mImgBtnDateSoldMin, mImgBtnDateSoldMax;
    private TextView mTxtViewDateAvailableMin, mTxtViewDateAvailableMax, mTxtViewDateSoldMin, mTxtViewDateSoldMax;

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
        configureSpinnerType();

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
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }

    private void configureViews(View view) {
        mSpinnerType = view.findViewById(R.id.spinner_type_activity_search);

        mEdtTxtPriceMin = view.findViewById(R.id.editText_activity_search_minimum_price);
        mEdtTxtPriceMax = view.findViewById(R.id.editText_activity_search_maximum_price);
        mEdtTxtSurfaceMin = view.findViewById(R.id.editTextNumber_activity_search_minimum_surface);
        mEdtTxtSurfaceMax = view.findViewById(R.id.editTextNumber_activity_search_maximum_surface);
        mEdtTxtRoomMin = view.findViewById(R.id.editTextNumber_activity_search_minimum_room);
        mEdtTxtRoomMax = view.findViewById(R.id.editTextNumber_activity_search_maximum_room);
        mImgBtnDateAvailableMin = view.findViewById(R.id.imageButton_activity_search_date_available_since);
        mImgBtnDateAvailableMax = view.findViewById(R.id.imageButton_activity_search_date_available_before);
        mImgBtnDateSoldMin = view.findViewById(R.id.imageButton_activity_search_date_sold_since);
        mImgBtnDateSoldMax = view.findViewById(R.id.imageButton_activity_search_date_sold_before);

        mTxtViewDateAvailableMin = view.findViewById(R.id.textView_date_available_since);
        mTxtViewDateAvailableMax = view.findViewById(R.id.textView_date_available_before);
        mTxtViewDateSoldMin = view.findViewById(R.id.textView_date_sold_since);
        mTxtViewDateSoldMax = view.findViewById(R.id.textView_date_sold_before);

        mFilterPropertyListBtn = view.findViewById(R.id.search_property_activity_button);
        mReInitiateFilterButton = view.findViewById(R.id.search_property_activity_button_reinitiate_filters);

    }

    private void configureSpinnerType() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(adapter);
    }

    private void configureLiveData() {
        LiveData<Date> dateAvailableMinLiveData = mViewModel.getDateAvailableMin();
        LiveData<Date> dateAvailableMaxLiveData = mViewModel.getDateAvailableMax();
        LiveData<Date> dateSoldMinLiveData = mViewModel.getDateSoldMin();
        LiveData<Date> dateSoldMaxLiveData = mViewModel.getDateSoldMax();
        LiveData<List<Property>> filteredPropertyListLiveData = mViewModel.getFilteredPropertyList();

        filteredPropertyListLiveData.observe(this, properties -> {
            mPropertyListFiltered = properties;
        });

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
    }

    // ------ LISTENER ------

    private void getFilterValueEntered() throws ParseException {
        type = mSpinnerType.getSelectedItem().toString();

        // ternary expression
        surfaceMin = mEdtTxtSurfaceMin.getText().toString().isEmpty()? 0 : Integer.parseInt(mEdtTxtSurfaceMin.getText().toString());
        surfaceMax = mEdtTxtSurfaceMax.getText().toString().isEmpty()? 99999 : Integer.parseInt(mEdtTxtSurfaceMax.getText().toString());
        priceMin = mEdtTxtPriceMin.getText().toString().isEmpty()? 0 : Integer.parseInt(mEdtTxtPriceMin.getText().toString());
        priceMax = mEdtTxtPriceMax.getText().toString().isEmpty()? 999999999: Integer.parseInt(mEdtTxtPriceMax.getText().toString());
        nbrRoomMin = mEdtTxtRoomMin.getText().toString().isEmpty()? 0: Integer.parseInt(mEdtTxtRoomMin.getText().toString());
        nbrRoomMax = mEdtTxtRoomMax.getText().toString().isEmpty()? 99: Integer.parseInt(mEdtTxtRoomMax.getText().toString());

        dateAvailableMin = mTxtViewDateAvailableMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("01/01/1900") : Utils.formatStringToDate(mTxtViewDateAvailableMin.getText().toString());

        dateAvailableMax = mTxtViewDateAvailableMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("31/12/2100") : Utils.formatStringToDate(mTxtViewDateAvailableMax.getText().toString());

        dateSoldMin = mTxtViewDateSoldMin.getText().toString().isEmpty()?
                Utils.formatStringToDate("01/01/1900"): Utils.formatStringToDate(mTxtViewDateSoldMin.getText().toString());

        dateSoldMax = mTxtViewDateSoldMax.getText().toString().isEmpty()?
                Utils.formatStringToDate("31/12/2100"): Utils.formatStringToDate(mTxtViewDateSoldMax.getText().toString());


        Log.d(TAG, "Filtered property type string value :" + type);

    }

    private void onClickFilterPropertyList() {
        mFilterPropertyListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getFilterValueEntered();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mViewModel.filterPropertyListAllType(surfaceMin, surfaceMax, priceMin, priceMax
                        ,nbrRoomMin,nbrRoomMax,dateAvailableMin ,dateAvailableMax,dateSoldMin ,dateSoldMax)
                        .observe(requireActivity(), properties ->{
                            mPropertyListFiltered.clear();
                            if (type.trim().isEmpty()){ // if user do not fill type filter, we return the list returned by the query
                                mPropertyListFiltered = properties;
                            } else {
                                for (int i = 0; i < properties.size() ; i++) {
                                    Log.d(TAG, "Filtered property size after query" +properties.size());
                                    // if user filled type filter, we add only properties matching with property type wanted
                                    if (properties.get(i).getType().equals(type)){
                                        mPropertyListFiltered.add(properties.get(i));
                                    }
                                }
                            }
                            mViewModel.getFilteredPropertyList().setValue(mPropertyListFiltered);
                            // pass list to main activity then to property list fragment filtered
                            mCallback.onClickFilterPropertiesListener(mPropertyListFiltered);
                            Log.d(TAG, "Filtered property size after type input :" + mPropertyListFiltered.size());
                        });
            }
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
        });
    }


    void onClickDateAvailablePicker(ImageButton imageButton, View view){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                int currentYear, currentMonth, currentDayOfMonth;
                Calendar calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_available_since)) {
                            mViewModel.getDateAvailableMin().setValue(calendar.getTime());
                        }
                        else if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_available_before)) {
                            mViewModel.getDateAvailableMax().setValue(calendar.getTime());
                        }
                        else if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_sold_since)) {
                            mViewModel.getDateSoldMin().setValue(calendar.getTime());
                        }
                        else if (imageButton == view.findViewById(R.id.imageButton_activity_search_date_sold_before)) {
                            mViewModel.getDateSoldMax().setValue(calendar.getTime());
                        }
                    }
                }, currentYear, currentMonth, currentDayOfMonth);
                datePickerDialog.show();
            }
        });
    }
}
