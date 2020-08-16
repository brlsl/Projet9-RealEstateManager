package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity {

    REMViewModel mViewModelGlobal;
    AddPropertyActivityViewModel mViewModel;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModelGlobal = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);

        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AddPropertyActivityViewModel.class);
    }

}
