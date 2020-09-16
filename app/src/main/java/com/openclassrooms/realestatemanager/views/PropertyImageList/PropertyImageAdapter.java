package com.openclassrooms.realestatemanager.views.PropertyImageList;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageViewHolder> {

    private List<Bitmap> mBitmapList;
    private List<String> mImageTitleList;
    private List<String> mImagePathList;

    public PropertyImageAdapter(List<Bitmap> bitmapList, List<String> imageTitleList, List<String> imagePathList) {
        mBitmapList = bitmapList;
        this.mImageTitleList = imageTitleList;
        mImagePathList = imagePathList;

    }

    @NonNull
    @Override
    public PropertyImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_property_photo_item, parent, false);
        return new PropertyImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyImageViewHolder holder, int position) {
        holder.displayData(mBitmapList, mImageTitleList, position);

        ImageButton delete = holder.itemView.findViewById(R.id.delete_photo_imageButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mBitmapList.size();
    }

    public void updatePhotoList(List<Bitmap> bitmapList){
        mBitmapList = bitmapList;
        notifyDataSetChanged();
    }

    public void updateTitleList(List<String> imageTitleList){
        mImageTitleList = imageTitleList;
        notifyDataSetChanged();
    }

    public void updatePathList(List<String> imagePathList){
        mImagePathList = imagePathList;
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        for (int i = 0; i <mImagePathList.size() ; i++) {
            Log.d("PropertyAdapter before:", position + mImagePathList.get(i).toString() + " " );
        }

        for (int i = 0; i <mBitmapList.size() ; i++) {
            Log.d("PropertyAdapter before:", position + mBitmapList.get(i).toString() + " " );
        }

        for (int i = 0; i <mBitmapList.size() ; i++) {
            Log.d("PropertyAdapter before:", mImageTitleList.get(i) + " ");
        }


        mBitmapList.remove(position);
        mImageTitleList.remove(position);
        mImagePathList.remove(position);
        notifyDataSetChanged();
        notifyItemRangeChanged(position, mBitmapList.size());
        notifyItemRangeChanged(position, mImageTitleList.size());

        for (int i = 0; i <mBitmapList.size() ; i++) {
            Log.d("PropertyAdapter before:", position + mImagePathList.get(i).toString() + " " );
        }

        for (int i = 0; i <mBitmapList.size() ; i++) {
            Log.d("PropertyAdapter after:", mBitmapList.get(i).toString() + " ");
        }

        for (int i = 0; i <mBitmapList.size() ; i++) {
            Log.d("PropertyAdapter after:", mImageTitleList.get(i) + " ");
        }


    }

}
