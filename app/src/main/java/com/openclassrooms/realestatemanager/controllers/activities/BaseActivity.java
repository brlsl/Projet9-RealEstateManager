package com.openclassrooms.realestatemanager.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


}
