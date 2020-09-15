package com.openclassrooms.realestatemanager.controllers.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Agent;

import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.views.AgentList.AgentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddAgentBottomSheetFragment extends BottomSheetDialogFragment {

    // FOR DATA
    private AgentAdapter mAdapter;
    private List<Agent> mAgentList = new ArrayList<>();
    private REMViewModel mViewModel;

    // FOR UI
    private RecyclerView mRecyclerView;


    // LISTENER
    private OnAgentItemClickListener mListener;

    public interface OnAgentItemClickListener{
        void onClickAgentItem(Agent agent);

    }

    // ----- FRAGMENT INSTANCE ------

    public static AddAgentBottomSheetFragment newInstance(){
        return new AddAgentBottomSheetFragment();
    }

    // ------ LIFE CYCLE ------

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OnAgentItemClickListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {

            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }

        });
        return dialog;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_agent_list_bottom_sheet_fragment,container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
        configureRecyclerView(view);
        configureClickOnAgentItem();
        onClickAddAgentFAB(view);

    }

    // ----- CONFIGURATION METHODS ------


    private void onClickAddAgentFAB(View view){
        FloatingActionButton addAgentFab = view.findViewById(R.id.add_agent_bottom_sheet_fragment_fab);
        addAgentFab.setOnClickListener(v -> showAddAgentDialogFragment());

    }

    private void showAddAgentDialogFragment(){
        AddAgentDialogFragment addAgentDialogFragment = new AddAgentDialogFragment();
        if (getFragmentManager() != null) {
            addAgentDialogFragment.show(getFragmentManager(), "Add agent dialog fragment");

        }
    }

    private void configureClickOnAgentItem() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.rv_agent_item_bottom_sheet_fragment)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    mListener.onClickAgentItem(mAgentList.get(position));
                    dismiss();
                    Toast.makeText(requireContext(), "Agent is chosen", Toast.LENGTH_SHORT).show();
                });
    }

    private void configureRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.agent_list_recyclerview);
        mAdapter = new AgentAdapter(mAgentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        // observe Room database changes and updates it in rv
        mViewModel.getAgentList().observe(this, agentList -> {
            mAdapter.updateAgentList(agentList);
            mAgentList = agentList;
        });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }
}
