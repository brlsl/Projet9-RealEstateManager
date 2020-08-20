package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertiesListFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.models.Property;

public class MainActivity extends BaseActivity implements PropertiesListFragment.OnItemPropertyClickListener {

    // FOR DATA
    public static final String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";
    public static final String PROPERTY_AGENT_ID_KEY = "PROPERTY_AGENT_ID_KEY";

    FragmentManager mFragmentManager;
    private boolean isTwoPane;
    //private REMViewModel mViewModel;

    PropertyDetailFragment mPropertyDetailFragment = new PropertyDetailFragment();

    // LIFE CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View detailView = findViewById(R.id.detail_container);

        // -----------------------
        mFragmentManager = getSupportFragmentManager();

        isTwoPane = findViewById(R.id.detail_container) != null && detailView.getVisibility() == View.VISIBLE;


        if (savedInstanceState == null) { // add fragment only once
            mFragmentManager.beginTransaction()
                    .add(R.id.container, new PropertiesListFragment())
                    .commit();
        }

        if (isTwoPane){
            mFragmentManager.beginTransaction()
                    .replace(R.id.container, new PropertiesListFragment())
                    .replace(R.id.detail_container, mPropertyDetailFragment).hide(mPropertyDetailFragment)
                    .commit();
        }


    }

    @Override
    public void onItemPropertySelected(Property property) {
        PropertyDetailFragment fragment = new PropertyDetailFragment();

        Bundle bundle = new Bundle();

        bundle.putLong(PROPERTY_ID_KEY, property.getAgentId());
        bundle.putLong(PROPERTY_AGENT_ID_KEY, property.getId());
        fragment.setArguments(bundle);


        if(isTwoPane){
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        }
        else{
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
