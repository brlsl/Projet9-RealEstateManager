package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.views.PropertyList.PropertiesAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchPropertyActivity extends AppCompatActivity {

    // FOR DATA
    private REMViewModel mViewModel;
    private static final String TAG = "SearchPropertyActivity";
    private List<Property> mPropertyListFiltered = new ArrayList<>();
    private int surfaceMin, surfaceMax, priceMin, priceMax, nbrRoomMin, nbrRoomMax;
    private String type;
    private Date dateAvailableMin, dateAvailableMax, dateSoldMin, dateSoldMax;


    // FOR UI
    private Button mFilterPropertyListBtn;
    private RecyclerView mRecyclerView;
    private PropertiesAdapter mAdapter;
    private EditText mEdtTxtPriceMin, mEdtTxtPriceMax, mEdtTxtSurfaceMin, mEdtTxtSurfaceMax, mEdtTxtRoomMin, mEdtTxtRoomMax;
    private Spinner mSpinnerType;
    private ImageButton mImgBtnDateAvailableMin, mImgBtnDateAvailableMax, mImgBtnDateSoldMin, mImgBtnDateSoldMax;
    private TextView mTxtViewDateAvailableMin, mTxtViewDateAvailableMax, mTxtViewDateSoldMin, mTxtViewDateSoldMax;

    public SearchPropertyActivity() throws ParseException {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);
        setTitle("Search Property");

        configureViewModel();
        configureLiveData();
        configureSpinnerType();
        configureViews();
        configureRecyclerView();


        onClickDateAvailablePicker(mImgBtnDateAvailableMin);
        onClickDateAvailablePicker(mImgBtnDateAvailableMax);
        onClickDateAvailablePicker(mImgBtnDateSoldMin);
        onClickDateAvailablePicker(mImgBtnDateSoldMax);


        onClickFilterPropertyList();

    }

    // ------ CONFIGURATION METHODS ------

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }

    private void configureSpinnerType() {
        mSpinnerType = findViewById(R.id.spinner_type_activity_search);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.property_activity_array_type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerType.setAdapter(adapter);
    }

    private void configureViews() {
        mEdtTxtPriceMin = findViewById(R.id.editText_activity_search_minimum_price);
        mEdtTxtPriceMax = findViewById(R.id.editText_activity_search_maximum_price);
        mEdtTxtSurfaceMin = findViewById(R.id.editTextNumber_activity_search_minimum_surface);
        mEdtTxtSurfaceMax = findViewById(R.id.editTextNumber_activity_search_maximum_surface);
        mEdtTxtRoomMin = findViewById(R.id.editTextNumber_activity_search_minimum_room);
        mEdtTxtRoomMax = findViewById(R.id.editTextNumber_activity_search_maximum_room);
        mImgBtnDateAvailableMin = findViewById(R.id.imageButton_activity_search_date_available_since);
        mImgBtnDateAvailableMax = findViewById(R.id.imageButton_activity_search_date_available_before);
        mImgBtnDateSoldMin = findViewById(R.id.imageButton_activity_search_date_sold_since);
        mImgBtnDateSoldMax = findViewById(R.id.imageButton_activity_search_date_sold_before);

        mTxtViewDateAvailableMin = findViewById(R.id.textView_date_available_since);
        mTxtViewDateAvailableMax = findViewById(R.id.textView_date_available_before);
        mTxtViewDateSoldMin = findViewById(R.id.textView_date_sold_since);
        mTxtViewDateSoldMax = findViewById(R.id.textView_date_sold_before);

        mFilterPropertyListBtn = findViewById(R.id.search_property_activity_button);

    }

    private void configureLiveData() {
        LiveData<Date> dateAvailableMinLiveData = mViewModel.getDateAvailableMin();
        LiveData<Date> dateAvailableMaxLiveData = mViewModel.getDateAvailableMax();
        LiveData<Date> dateSoldMinLiveData = mViewModel.getDateSoldMin();
        LiveData<Date> dateSoldMaxLiveData = mViewModel.getDateSoldMax();
        LiveData<List<Property>> filteredPropertyListLiveData = mViewModel.getFilteredPropertyList();

        filteredPropertyListLiveData.observe(this, properties -> {
            mAdapter.updatePropertyList(properties);
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

    private void configureRecyclerView() {
        mRecyclerView = findViewById(R.id.activity_search_recycler_view_properties_filtered);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertiesAdapter(mPropertyListFiltered);

        mRecyclerView.setAdapter(mAdapter);

        Log.d(TAG, "Filtered property recycler view construction :" + mPropertyListFiltered.size());

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
        Toast.makeText(this, "FBAO", Toast.LENGTH_SHORT).show();

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
                        .observe(SearchPropertyActivity.this, properties ->{
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
                            mAdapter.updatePropertyList(mPropertyListFiltered);
                            mRecyclerView.setAdapter(mAdapter);
                            Log.d(TAG, "Filtered property size after type input :" + mPropertyListFiltered.size());
                        });
            }
        });
    }

    void onClickDateAvailablePicker(ImageButton imageButton){
        imageButton.setOnClickListener(view -> {
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

                    if (imageButton == findViewById(R.id.imageButton_activity_search_date_available_since)){
                        mViewModel.getDateAvailableMin().setValue(calendar.getTime());
                    }

                    if (imageButton == findViewById(R.id.imageButton_activity_search_date_available_before)){
                        mViewModel.getDateAvailableMax().setValue(calendar.getTime());
                    }

                    if (imageButton == findViewById(R.id.imageButton_activity_search_date_sold_since)){
                        mViewModel.getDateSoldMin().setValue(calendar.getTime());
                    }

                    if (imageButton == findViewById(R.id.imageButton_activity_search_date_sold_before)){
                        mViewModel.getDateSoldMax().setValue(calendar.getTime());
                    }

                }
            }, currentYear, currentMonth, currentDayOfMonth);
            datePickerDialog.show();
        });
    }
}