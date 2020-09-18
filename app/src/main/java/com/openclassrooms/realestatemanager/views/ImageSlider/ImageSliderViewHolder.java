package com.openclassrooms.realestatemanager.views.ImageSlider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Image;

import java.io.File;

public class ImageSliderViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;
    private Context mContext;
    private TextView mTxtViewImageTitle;

    public ImageSliderViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_slider_item);
        mContext = itemView.getContext();
        mTxtViewImageTitle = itemView.findViewById(R.id.image_slider_item_title);
    }

    public void setImage(Image image){
       File imgFile = new File(image.getImagePath());
       if (imgFile.exists()){
           if (imgFile.exists()) {
               Glide.with(mContext)
                       .load(imgFile)
                       .centerCrop()
                       .into(mImageView);
           }
       }
       mTxtViewImageTitle.setText(image.getImageTitle());
    }
}
