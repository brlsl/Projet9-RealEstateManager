package com.openclassrooms.realestatemanager.controllers.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.openclassrooms.realestatemanager.R;

import java.util.Objects;

public class AddAgentDialogFragment extends DialogFragment {

    private Dialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.add_agent_dialog, null);



        builder.setView(view)
                .setTitle("Add new Agent")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", null);
        Dialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                Toast.makeText(requireContext(), "Agent added", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show();


        return dialog;
    }
}
