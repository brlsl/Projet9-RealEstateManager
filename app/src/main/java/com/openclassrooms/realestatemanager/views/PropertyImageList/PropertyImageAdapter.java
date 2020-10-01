package com.openclassrooms.realestatemanager.views.PropertyImageList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.AddPhotoTitleDialogFragment;

import java.util.List;
import java.util.Objects;

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageViewHolder> {

    private List<Bitmap> mBitmapList;
    private List<String> mImageTitleList;
    private List<String> mImagePathList;
    private FragmentManager fm;
    private Context mContext;


    public PropertyImageAdapter(List<Bitmap> bitmapList, List<String> imageTitleList,
                                List<String> imagePathList, FragmentManager supportFragmentManager, Context context) {
        mBitmapList = bitmapList;
        mImageTitleList = imageTitleList;
        mImagePathList = imagePathList;
        fm = supportFragmentManager;
        mContext = context;
    }

    // empty constructor required
    public PropertyImageAdapter() {
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

        TextView textViewTitle = holder.itemView.findViewById(R.id.photo_title_item_textView);
        textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoTitleDialogFragment fragment = new AddPhotoTitleDialogFragment(position, mImageTitleList, mContext);
                if (fm != null){
                    fragment.show(fm, "Add photo dialog");
                }
            }
        });


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
        Log.d("PropertyAdapter posit:", String.valueOf(position));

        mBitmapList.remove(position);
        mImageTitleList.remove(position);
        mImagePathList.remove(position);

        notifyItemRemoved(position);
        notifyItemChanged(position);

        notifyDataSetChanged();
        notifyItemRangeChanged(position, mBitmapList.size());
        notifyItemRangeChanged(position, mImageTitleList.size());
    }


}
