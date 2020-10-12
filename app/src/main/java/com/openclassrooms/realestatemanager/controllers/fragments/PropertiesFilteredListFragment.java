package com.openclassrooms.realestatemanager.controllers.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.activities.AddPropertyActivity;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.views.PropertyList.PropertiesAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.controllers.activities.MainActivity.PROPERTY_LIST_FILTERED_KEY;

public class PropertiesFilteredListFragment extends Fragment {

    // FOR DATA
    private List<Property> mFilteredPropertiesList = new ArrayList<>();

    // FOR UI
    private RecyclerView mRecyclerView;
    private TextView mTxtViewFilteredListResult;
    private FloatingActionButton mFabAddProperty;

    // CALLBACK
    private PropertiesListFragment.OnItemPropertyClickListener mCallback;

    // LIFECYCLE

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Now the Fragment needs to get an instance to the MainActivity that implements OnItemPropertyClickListener.
        try {
            mCallback = (PropertiesListFragment.OnItemPropertyClickListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement SettingOptionsFragment.OnItemPropertyClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rv_properties_filtered_list,container,false);

        configureViews(view);
        configureRecyclerView(view);
        configureOnClickRecyclerView();

        onClickFab();

        return view;
    }

    // ------ CONFIGURATION METHODS ------

    private void configureViews(View view){
        mTxtViewFilteredListResult = view.findViewById(R.id.textView_properties_filtered_list);
        mFabAddProperty = view.findViewById(R.id.fragment_properties_filtered_add_property_fab);
    }

    private void configureRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.fragment_properties_filtered_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        PropertiesAdapter mAdapter = new PropertiesAdapter(mFilteredPropertiesList);
        mRecyclerView.setAdapter(mAdapter);

        // get arguments from Search Property Fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<Property> list = bundle.getParcelableArrayList(PROPERTY_LIST_FILTERED_KEY);
            if (list != null) {
                mFilteredPropertiesList = list;
                if (list.size() == 0)
                    mTxtViewFilteredListResult.setText(this.getString(R.string.filter_0_result));
                if (list.size() == 1)
                    mTxtViewFilteredListResult.setText(this.getString(R.string.filter_one_result_found, list.size()));
                if (list.size() > 1)
                    mTxtViewFilteredListResult.setText(this.getString(R.string.filter_results_found ,list.size()));

                mAdapter.updatePropertyList(list);
            }
        }
    }

    // ------ LISTENERS ------

    private void onClickFab() {
        mFabAddProperty.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddPropertyActivity.class);
            startActivity(intent);
        });
    }


    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.rv_property_item)
                .setOnItemClickListener((recyclerView, position, v) -> mCallback.onItemPropertySelected(mFilteredPropertiesList.get(position)));
    }
}
