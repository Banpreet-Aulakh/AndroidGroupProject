package com.sfu.cmpt276.coopachievement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PermissionsFragment extends AppCompatDialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View v = LayoutInflater.from(getActivity())
               .inflate(R.layout.permissions_alert_layout, null);

       DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("TAG", "You clicked the dialog button");
            }
       };

       return new AlertDialog.Builder(getActivity())
               .setTitle("Permissions Needed")
               .setView(v)
               .setPositiveButton(android.R.string.ok, listener)
               .create();
    }
}
