package com.openclassrooms.realestatemanager.views.ImageSlider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Image;

import java.io.File;

public class ImageSliderViewHolder extends RecyclerView.ViewHolder {
    private ImageView mImageView;

    public ImageSliderViewHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_slider_item);
    }

    public void setImage(Image image){
       File imgFile = new File(image.getImagePath());
       if (imgFile.exists()){
           Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

           mImageView.setImageBitmap(myBitmap);
           mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       }
    }
}
