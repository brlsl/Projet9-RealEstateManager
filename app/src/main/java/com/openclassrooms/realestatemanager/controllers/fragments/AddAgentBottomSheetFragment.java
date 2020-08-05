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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.views.AgentList.AgentAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddAgentBottomSheetFragment extends BottomSheetDialogFragment {

    // FOR DATA
    private AgentAdapter mAdapter;
    List<Agent> mAgentList = new ArrayList<>();
    REMViewModel mAgentViewModel;

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

    }

    // ----- CONFIGURATION METHODS ------

    private void configureClickOnAgentItem() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.rv_agent_item_bottom_sheet_fragment)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        mListener.onClickAgentItem(mAgentList.get(position));
                        Toast.makeText(requireContext(), "Agent is chosen", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configureRecyclerView(View view) {
       /*
        Agent agent = new Agent(2,"nameTest","surnameTest");
        Agent agent2 = new Agent(3,"nameTest2","surnameTest2");
        mAgentList.add(agent);
        mAgentList.add(agent2);
        mAgentList.add(agent);
        mAgentList.add(agent2);
        mAgentList.add(agent);
        mAgentList.add(agent2);
        mAgentList.add(agent);
        mAgentList.add(agent2);
        mAgentList.add(agent);
        mAgentList.add(agent2);


        */

        mRecyclerView = view.findViewById(R.id.agent_list_recyclerview);
        mAdapter = new AgentAdapter(mAgentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        // observe Room database changes(add, update) and updates it in rv
        mAgentViewModel.getAgentList().observe(this, new Observer<List<Agent>>() {
            @Override
            public void onChanged(List<Agent> agentList) {
                mAdapter.updateAgentList(agentList);
                mAgentList = agentList;
            }
        });
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mAgentViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
    }
}
