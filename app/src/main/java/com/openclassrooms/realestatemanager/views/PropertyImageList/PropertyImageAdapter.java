package com.openclassrooms.realestatemanager.views.PropertyImageList;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.List;

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageViewHolder> {

    private List<Bitmap> mBitmapList;
    private List<String> mImageTitleList;

    public PropertyImageAdapter(List<Bitmap> bitmapList, List<String> imageTitleList) {
        mBitmapList = bitmapList;
        mImageTitleList = imageTitleList;
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

        EditText editText = holder.itemView.findViewById(R.id.photo_description_item_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    public void removeAt(int position) {
        mBitmapList.remove(position);
        mImageTitleList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mBitmapList.size());
    }

}
