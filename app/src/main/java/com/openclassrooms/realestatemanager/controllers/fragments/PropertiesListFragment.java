package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.views.PropertiesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.views.PropertiesViewHolder.PROPERTY_CITY;
import static com.openclassrooms.realestatemanager.views.PropertiesViewHolder.PROPERTY_PRICE;

public class PropertiesListFragment extends BaseFragment {

    // FOR DATA
    private RecyclerView mRecyclerView;
    private PropertiesAdapter mAdapter;
    List<Property> mPropertiesList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_properties_list,container,false);

        configureRecyclerView(view);
        configureOnClickRecyclerView();

        return view;
    }

    private void configureRecyclerView(View view) {
        mPropertiesList.add(new Property("Paris",123));
        mPropertiesList.add(new Property("Marseille",456));
        mPropertiesList.add(new Property("Toulouse",789));
        mPropertiesList.add(new Property("Rennes",1011));
        mPropertiesList.add(new Property("Lille",1213));
        mPropertiesList.add(new Property("Strasbourg",1415));
        mPropertiesList.add(new Property("Montpellier",1617));
        mPropertiesList.add(new Property("Strasbourg",1819));
        mPropertiesList.add(new Property("Troyes",2021));
        mPropertiesList.add(new Property("Dijon",2223));

        mRecyclerView = view.findViewById(R.id.properties_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new PropertiesAdapter(mPropertiesList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.rv_item_property)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
/*
                        AppCompatActivity activity = (AppCompatActivity) getActivity();

                        PropertyDetailFragment fragment = new PropertyDetailFragment();

                        // pass data to other fragment
                        Bundle bundle = new Bundle();
                        bundle.putString(PROPERTY_CITY, mPropertiesList.get(position).getCity());
                        bundle.putInt(PROPERTY_PRICE, mPropertiesList.get(position).getPrice());
                        fragment.setArguments(bundle);

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit();


 */
                        mCallback.onOptionSelected(mPropertiesList.get(position));
                    }
                });
    }

    public interface OnOptionClickListener{
        void onOptionSelected(Property property);
    }

    private OnOptionClickListener mCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // 2
        // Now the SettingOptionsFragment needs to get an instance to the MainActivity that implements OnOptionClickListener.
        // You can grab the Activity in the onAttach method of a fragment.
        try {
            mCallback = (OnOptionClickListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement SettingOptionsFragment.OnOptionClickListener");
        }
    }
}
