package com.openclassrooms.realestatemanager.controllers.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends AppCompatActivity {

    AddPropertyActivityViewModel mAddPropertyViewModel;
    EditPropertyActivityViewModel mEditPropertyViewModel;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);

        mAddPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(AddPropertyActivityViewModel.class);
        mEditPropertyViewModel = new ViewModelProvider(this, viewModelFactory).get(EditPropertyActivityViewModel.class);

    }



    void descriptionTitleLengthListener(EditText editText, TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int maxLength = 300;
                textView.setText("Description (" + (maxLength - editText.length()) + "/300)");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
