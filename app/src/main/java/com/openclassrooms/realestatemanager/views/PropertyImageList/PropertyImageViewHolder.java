
package com.openclassrooms.realestatemanager.views.PropertyImageList;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class PropertyImageViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImageView;
    private TextView mTextViewTitle;

    public PropertyImageViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.property_imageView_item);
        mTextViewTitle = itemView.findViewById(R.id.photo_title_item_textView);
    }

    public void displayData(List<Bitmap> mBitmapList, List<String> mPhotoTitleList, int position) {
        mImageView.setImageBitmap(mBitmapList.get(position));

        mTextViewTitle.setText(mPhotoTitleList.get((position)));

    }


}
