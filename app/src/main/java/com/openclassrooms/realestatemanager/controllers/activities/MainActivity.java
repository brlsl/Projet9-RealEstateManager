package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.controllers.fragments.PropertiesFilteredListFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertiesListFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.DetailPropertyFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.SearchPropertyFragment;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ExamUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PropertiesListFragment.OnItemPropertyClickListener,
        NavigationView.OnNavigationItemSelectedListener, SearchPropertyFragment.OnClickFilterPropertyList {

    // FOR DATA
    public static final String TAG = "MAIN_ACTIVITY";
    public static final String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";
    public static final String PROPERTY_AGENT_ID_KEY = "PROPERTY_AGENT_ID_KEY";
    public static final String PROPERTY_LIST_FILTERED_KEY = "PROPERTY_FILTERED_LIST";
    private static final String DETAIL_FRAGMENT_PORTRAIT_TAG = "DETAIL_FRAGMENT_TAG";
    private static final String SEARCH_PROPERTY_TAG = "SEARCH_PROPERTY_FRAGMENT";
    private static final String DETAIL_FRAGMENT_PORTRAIT_COPY = "DETAIL_FRAGMENT_COPY_TAG" ;
    private static final String DETAIL_FRAGMENT_LAND_COPY = "DETAIL_FRAGMENT_LAND_COPY_TAG";

    private FragmentManager mFragmentManager;
    private boolean isTwoPane;
    private DetailPropertyFragment mDetailPropertyFragment = new DetailPropertyFragment();
    private long mPropertyId = -1, mAgentId = -1;

    // FOR UI
    private androidx.appcompat.widget.Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    // ------ LIFE CYCLE ------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureToolBar();
        configureDrawerLayout();
        configureNavigationViewListener();
        configureDualPaneLayout(savedInstanceState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // make toolbar menu item appears
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // ------ CONFIGURATION ------



    private void configureToolBar() {
        this.mToolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(mToolbar);
    }

    private void configureDrawerLayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout_main_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //final PropertiesListFragment mPropertiesListFragment = new PropertiesListFragment();

    private void configureDualPaneLayout(Bundle savedInstanceState) {
        View detailView = findViewById(R.id.detail_container);

        // -----------------------
        mFragmentManager = getSupportFragmentManager();

        isTwoPane = findViewById(R.id.detail_container) != null && detailView.getVisibility() == View.VISIBLE;


        if (savedInstanceState == null) { // add fragment only once

            mFragmentManager.beginTransaction()
                    .add(R.id.container_list, new PropertiesListFragment())
                    .commit();

/*
            PropertiesListFragment test = new PropertiesListFragment();
            mPropertiesListFragment.setArguments(getIntent().getExtras());
            mFragmentManager.beginTransaction()
                    .replace(R.id.container_list, test, "test_tag")
                    .commit();


 */
        }

        if (isTwoPane) {
            // in case rotation, retrieve detail fragment
            DetailPropertyFragment fragmentDetailPortrait = (DetailPropertyFragment) mFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_PORTRAIT_COPY);
            DetailPropertyFragment fragmentDetailLand = (DetailPropertyFragment) mFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_LAND_COPY);


            if (fragmentDetailPortrait != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.container_list, new PropertiesListFragment())
                        .replace(R.id.detail_container, fragmentDetailPortrait)
                        .addToBackStack(null)
                        .commit();

                Log.d(TAG, "Portrait debug");

            } else if (fragmentDetailLand != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.container_list, new PropertiesListFragment())
                        .replace(R.id.detail_container, fragmentDetailLand)
                        .addToBackStack(null)
                        .commit();
                Log.d(TAG, "Land debug");
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.container_list, new PropertiesListFragment())
                        .replace(R.id.detail_container, new Fragment())
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    private void configureNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.navigation_view_main_activity);
        navigationView.setNavigationItemSelectedListener(this);
    }


    // ------ LISTENERS ------

    @Override
    public void onItemPropertySelected(Property property) {
        Bundle bundle = new Bundle();
        DetailPropertyFragment detailFragment = new DetailPropertyFragment();
        DetailPropertyFragment detailFragmentCopy = new DetailPropertyFragment();
        if (!isTwoPane){
            bundle.putLong(PROPERTY_ID_KEY, property.getId());
            bundle.putLong(PROPERTY_AGENT_ID_KEY, property. getAgentId());
            detailFragment.setArguments(bundle);

            detailFragmentCopy.setArguments(bundle);

            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container_list, detailFragment, DETAIL_FRAGMENT_PORTRAIT_TAG)
                    .replace(R.id.detail_container,detailFragmentCopy, DETAIL_FRAGMENT_PORTRAIT_COPY) // need a copy because can't change container of an existing fragment
                    .addToBackStack(null)
                    .commit();
        }
        else if (mPropertyId != property.getId() || mAgentId != property.getAgentId()) { // prevent fragment recreation
            mPropertyId = property.getId();
            mAgentId = property.getAgentId();

            bundle.putLong(PROPERTY_ID_KEY, mPropertyId);
            bundle.putLong(PROPERTY_AGENT_ID_KEY, mAgentId);
            detailFragment.setArguments(bundle);

            detailFragmentCopy.setArguments(bundle);

                if (detailFragment != mDetailPropertyFragment) {
                    mDetailPropertyFragment = detailFragment;

                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.detail_container, detailFragment, DETAIL_FRAGMENT_LAND_COPY)
                            .addToBackStack(null)
                            .commit();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (isTwoPane) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
       /* // after multiple rotation, fragment are added to backstack, if > 1 and detail not visible, finish activity in portrait
       else if (mFragmentManager.getBackStackEntryCount() > 1 && (
                !Objects.requireNonNull(mFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_KEY)).isVisible()) ||
                !Objects.requireNonNull(mFragmentManager.findFragmentByTag(SEARCH_PROPERTY_KEY)).isVisible()){
            finish();


        }
        */
        else
            super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // toolbar item
        int id = item.getItemId();
        if (id == R.id.search_property_toolbar) {
            if (!isTwoPane) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.container_list, new SearchPropertyFragment(), SEARCH_PROPERTY_TAG)
                        .addToBackStack(null)
                        .commit();

            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.detail_container, new SearchPropertyFragment(), SEARCH_PROPERTY_TAG)
                        .addToBackStack(null)
                        .commit();

            }
        }

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) { // menu drawer
        int id = item.getItemId();
        switch (id){
            case R.id.activity_main_drawer_loan:
                Intent loanIntent = new Intent(this, LoanSimulationActivity.class);
                startActivity(loanIntent);
                break;
            case R.id.activity_main_drawer_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.activity_main_map:
                boolean isInternetAvailable = ExamUtils.isInternetAvailable(this);
                if (isInternetAvailable){
                    Intent mapIntent = new Intent(this, MapActivity.class);
                    startActivity(mapIntent);
                }
                else {
                    Toast.makeText(this, "Please check your internet connexion", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);  //close navigation drawer after choice
        return true;
    }


    @Override
    public void onClickFilterPropertiesListener(List<Property> propertyList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PROPERTY_LIST_FILTERED_KEY, (ArrayList<Property>) propertyList);

        PropertiesFilteredListFragment filteredListFragment = new PropertiesFilteredListFragment();
        filteredListFragment.setArguments(bundle);

        mFragmentManager
                .beginTransaction()
                .replace(R.id.container_list, filteredListFragment)
                .addToBackStack(null)
                .commit();
    }
}
