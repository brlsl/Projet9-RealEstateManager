package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.openclassrooms.realestatemanager.controllers.fragments.PropertiesListFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.PropertyDetailFragment;
import com.openclassrooms.realestatemanager.models.Property;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PropertiesListFragment.OnItemPropertyClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    // FOR DATA
    public static final String PROPERTY_ID_KEY = "PROPERTY_ID_KEY";
    public static final String PROPERTY_AGENT_ID_KEY = "PROPERTY_AGENT_ID_KEY";
    private static final String DETAIL_FRAGMENT_KEY = "DETAIL_FRAGMENT_KEY";

    private FragmentManager mFragmentManager;
    private boolean isTwoPane;
    //private REMViewModel mViewModel;
    PropertyDetailFragment mPropertyDetailFragment = new PropertyDetailFragment();
    long mPropertyId = -1, mAgentId = -1;

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
                        .replace(R.id.detail_container, new Fragment())
                        .addToBackStack(null)
                        .commit();
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
        PropertyDetailFragment detailFragment = new PropertyDetailFragment();
        if (!isTwoPane){
            bundle.putLong(PROPERTY_ID_KEY, property.getId());
            bundle.putLong(PROPERTY_AGENT_ID_KEY, property.getAgentId());
            detailFragment.setArguments(bundle);

            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, detailFragment, DETAIL_FRAGMENT_KEY)
                    .addToBackStack(null)
                    .commit();
        }
        else if (mPropertyId != property.getId() || mAgentId != property.getAgentId()) { // prevent fragment recreation
            mPropertyId = property.getId();
            mAgentId = property.getAgentId();

            bundle.putLong(PROPERTY_ID_KEY, mPropertyId);
            bundle.putLong(PROPERTY_AGENT_ID_KEY, mAgentId);
            detailFragment.setArguments(bundle);

                if (detailFragment != mPropertyDetailFragment) {
                    mPropertyDetailFragment = detailFragment;
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.detail_container, detailFragment)
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
        // after multiple rotation, fragment are added to backstack, if > 1 and detail not visible, finish activity in portrait
       else if (mFragmentManager.getBackStackEntryCount() > 1 &&
                !Objects.requireNonNull(mFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_KEY)).isVisible()){
            finish();
        }
        else
            super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // toolbar item
        int id = item.getItemId();
        switch (id){
            case R.id.search_property_toolbar:
                Toast.makeText(this, "Open search property", Toast.LENGTH_SHORT).show();
                break;
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
                Toast.makeText(this, "Open loan activity", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_main_drawer_settings:
                Intent SettingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(SettingsIntent);
                Toast.makeText(this, "Open settings", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);  //close navigation drawer after choice
        return true;
    }

}
