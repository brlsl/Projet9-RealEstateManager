package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    long mPropertyId = -1, mAgentId = -1;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu); // make toolbar menu item appear
        return true;
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
                        .commit();
        }
    }

    @Override
    public void onItemPropertySelected(Property property) {
        if (mPropertyId != property.getId() || mAgentId != property.getAgentId()) { // prevent fragment recreation
            mPropertyId = property.getId();
            mAgentId = property.getAgentId();

            Bundle bundle = new Bundle();
            bundle.putLong(PROPERTY_ID_KEY, mPropertyId);
            bundle.putLong(PROPERTY_AGENT_ID_KEY, mAgentId);
            PropertyDetailFragment fragment = new PropertyDetailFragment();
            fragment.setArguments(bundle);

            if(isTwoPane){
                if (fragment != mPropertyDetailFragment) {
                    mPropertyDetailFragment = fragment;
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.detail_container, fragment)
                            .commit();
                }
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.search_property_toolbar:
                Toast.makeText(this, "Open search property", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }
}
