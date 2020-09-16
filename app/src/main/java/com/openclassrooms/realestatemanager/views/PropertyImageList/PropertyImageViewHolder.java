
package com.openclassrooms.realestatemanager.views.PropertyImageList;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class PropertyImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    private EditText mEditTextTitle;

    public PropertyImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.property_imageView_item);
        mEditTextTitle = itemView.findViewById(R.id.photo_description_item_editText);
    }


    public void displayData(List<Bitmap> mBitmapList, List<String> mPhotoTitleList, int position) {
        mImageView.setImageBitmap(mBitmapList.get(position));

        mEditTextTitle.setText(mPhotoTitleList.get((position)));

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            mPhotoTitleList.set(position, editable.toString());

            }
        });

    }


}
