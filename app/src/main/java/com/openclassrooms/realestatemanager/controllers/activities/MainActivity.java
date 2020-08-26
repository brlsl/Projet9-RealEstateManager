package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
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

    // FOR UI
    private androidx.appcompat.widget.Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    // LIFE CYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolBar();
        configureDrawerLayout();
        configureDualPaneLayout(savedInstanceState);


    }

    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout_main_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureDualPaneLayout(Bundle savedInstanceState) {
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

        bundle.putLong(PROPERTY_ID_KEY, property.getId());
        bundle.putLong(PROPERTY_AGENT_ID_KEY, property.getAgentId());
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

    @Override
    public void onBackPressed() {
        if (isTwoPane) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else
            super.onBackPressed();
    }

    private void configureToolBar() {
        this.mToolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);
    }
}
