package com.openclassrooms.realestatemanager.views.PropertyList;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;

import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ExamUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;

import static com.openclassrooms.realestatemanager.controllers.activities.SettingsActivity.SettingsFragment.PREFERENCES_CURRENCY;

public class PropertiesViewHolder extends RecyclerView.ViewHolder{

    // FOR DATA
    private Context mContext;

    // FOR UI
    private TextView mPropertyPrice, mPropertyCity, mPropertyType, mSoldTxtView, mCurrencyTxtView;
    private ImageView mPropertyImageView;

    public PropertiesViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mPropertyPrice = itemView.findViewById(R.id.properties_list_property_price);
        mPropertyCity = itemView.findViewById(R.id.properties_list_property_city);
        mPropertyType = itemView.findViewById(R.id.properties_list_property_type);
        mPropertyImageView = itemView.findViewById(R.id.properties_list_property_imageview);
        mSoldTxtView = itemView.findViewById(R.id.properties_list_property_is_sold);
        mCurrencyTxtView = itemView.findViewById(R.id.properties_list_currency);
    }

    public void displayData(final Property property) {
        mPropertyCity.setText(property.getCity());

        mPropertyType.setText(property.getType());

        mPropertyPrice.setText(Utils.formatPrice(String.valueOf(property.getPrice())));

        mCurrencyTxtView.setText(property.getCurrency());

     //   int price = property.getPrice();
/*
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isDollar = preferences.getBoolean(PREFERENCES_CURRENCY,false);
        if (isDollar){
            price = ExamUtils.convertEuroToDollar(price);
            mCurrencyTxtView.setText(mContext.getString(R.string.dollar));
        } else
        {
            price = ExamUtils.convertDollarToEuro(price);
            mCurrencyTxtView.setText(mContext.getString(R.string.euro));
        }


*/
        File file = new File(property.getMainImagePath());
        if (file.exists()) {
            Glide.with(mContext)
                    .load(file)
                    .centerCrop()
                    .into(mPropertyImageView);
        }
        else {
            Glide.with(mContext)
                    .load(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(mPropertyImageView);
        }


        if (property.isAvailable()){
            mSoldTxtView.setVisibility(View.GONE);
        } else {
            mSoldTxtView.setVisibility(View.VISIBLE);
        }

    }
}
