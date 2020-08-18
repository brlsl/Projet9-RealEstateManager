package com.openclassrooms.realestatemanager.controllers.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Agent;

import java.util.Objects;

public class AddAgentDialogFragment extends DialogFragment {

    private Dialog dialog;
    private  AlertDialog.Builder builder;
    private EditText mAgentName, mAgentSurname;
    private AddAgentDialogFragmentViewModel mViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.add_agent_dialog, null);

        mAgentName = view.findViewById(R.id.add_agent_dialog_agent_name);
        mAgentSurname = view.findViewById(R.id.add_agent_dialog_agent_surname);

        configureViewModel();
        buildDialog(view);

        return dialog;
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(AddAgentDialogFragmentViewModel.class);
    }

    private void buildDialog(View view) {
        builder.setView(view)
                .setTitle("Add new Agent")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", null);

        dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {
                        String agentName = mAgentName.getText().toString();
                        String agentSurname = mAgentSurname.getText().toString();

                        if (!(agentName.isEmpty() || agentSurname.isEmpty())){
                            Agent agent = new Agent(agentName, agentSurname);
                            mViewModel.createAgent(agent);
                            dialog.dismiss();
                            Toast.makeText(requireContext(), "Agent added", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(requireContext(), "Please fill all forms", Toast.LENGTH_SHORT).show();
                    });
        });

        dialog.setCanceledOnTouchOutside(false);

    }

}
