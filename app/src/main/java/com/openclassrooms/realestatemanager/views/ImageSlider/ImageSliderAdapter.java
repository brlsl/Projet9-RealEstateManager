package com.openclassrooms.realestatemanager.views.ImageSlider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Image;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderViewHolder> {

    private List<Image> mImageList;

    public ImageSliderAdapter(List<Image> mImageList) {
        this.mImageList = mImageList;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.setImage(mImageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }
}
