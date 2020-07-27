package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertiesListFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.models.Property;

import static com.openclassrooms.realestatemanager.views.PropertiesViewHolder.PROPERTY_CITY;
import static com.openclassrooms.realestatemanager.views.PropertiesViewHolder.PROPERTY_PRICE;


public class MainActivity extends BaseActivity implements PropertiesListFragment.OnItemPropertyClickListener {

    // FOR DATA
    FragmentManager mFragmentManager;
    private boolean isTwoPane;


    // LIFE CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.detail_container) != null) {
            isTwoPane = true;
        }
        else{
            isTwoPane = false;
        }

        if (savedInstanceState == null) { // add fragment only once
            mFragmentManager.beginTransaction()
                    .add(R.id.container, new PropertiesListFragment())
                    .commit();
        }
    }


    @Override
    public void onItemPropertySelected(Property property) {
        PropertyDetailFragment fragment = new PropertyDetailFragment();

        // pass data to other fragment
        Bundle bundle = new Bundle();
        bundle.putString(PROPERTY_CITY, property.getCity());
        bundle.putInt(PROPERTY_PRICE, property.getPrice());
        fragment.setArguments(bundle);

        if(isTwoPane){

            this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_container, fragment)
                .commit();
        }
        else{
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
