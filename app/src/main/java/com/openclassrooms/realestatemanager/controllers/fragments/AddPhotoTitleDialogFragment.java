package com.openclassrooms.realestatemanager.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.activities.BasePropertyActivityViewModel;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;

import java.util.List;
import java.util.Objects;

public class AddPhotoTitleDialogFragment extends DialogFragment {

    // FOR UI
    private Dialog dialog;
    private EditText mPhotoTitleDescriptionEdtTxt;

    // FOR DATA
    private static final String TAG = "PhotoTitleDialog";
    private List<String> mImageTitleList;
    private Integer mPosition;
    private Context mContext;
    private Integer copyPosition;
    private List<String> mCopyImageTitleList;
    private BasePropertyActivityViewModel mViewModel;

    public interface OnClickDialogConfirmListener {
        void onConfirmClick(List<String> imageTitleList);
    }

    // LISTENER
    public OnClickDialogConfirmListener mListener;

    // CONSTRUCTOR

    public AddPhotoTitleDialogFragment(){
        // empty constructor need in case orientation change
    }

    public AddPhotoTitleDialogFragment(Integer position, List<String> imageTitleList, Context context) {
        mImageTitleList = imageTitleList;
        mPosition = position;
        mContext = context;
    }

    // LIFE CYCLE
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (OnClickDialogConfirmListener) context;
        } catch (Exception e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement OnClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        configureViewModel();

        // get data once to view model
        if (savedInstanceState == null) {
            mViewModel.getImageTitleList().setValue(mImageTitleList);
            mViewModel.getPosition().setValue(mPosition);
        }

        LiveData<Integer> positionLiveData = mViewModel.getPosition();
        LiveData<List<String>> imageTitleListLiveData = mViewModel.getImageTitleList();

        positionLiveData.observe(this, integer -> {
            copyPosition = integer;
            Log.d(TAG,"Dialog live integer copy " + copyPosition);
        });

        imageTitleListLiveData.observe(this, strings -> {
            mCopyImageTitleList = strings;
            Log.d(TAG,"Dialog list title live copy  " + mCopyImageTitleList);
        });

        buildDialog();

        return dialog;
    }

    // CONFIGURATION METHODS

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(mContext);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(BasePropertyActivityViewModel.class);
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.add_photo_title_dialog, null);

        mPhotoTitleDescriptionEdtTxt = view.findViewById(R.id.add_photo_title_dialog_editText_title);

        // build dialog
        builder.setView(view)
                .setTitle("Add Title Description")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", null);

        dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        if (mPhotoTitleDescriptionEdtTxt.getText().toString().length() > 0) {
                            String photoTitle =  mPhotoTitleDescriptionEdtTxt.getText().toString();
                            mCopyImageTitleList.set(copyPosition, photoTitle);
                            mListener.onConfirmClick(mCopyImageTitleList);

                            dialog.dismiss();
                        }
                    });
        });
        dialog.setCanceledOnTouchOutside(false);
    }

}
