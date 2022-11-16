package com.sfu.cmpt276.coopachievement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


/*
 * Message Fragment Class: Display congratulations message once player found all mines
 */

public class MessageFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view by loading layout
        @SuppressLint("InflateParams") View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.message_fragment_layout,null);

        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //create dialog when winning to for escaping game, or change setting
                        TextView tv = getActivity().findViewById(R.id.confirm_text);
                        tv.setText("Are you sure about that?");
                        getActivity().finish(); // call only when needed
                        break;

                }
            }
        };

        //Build Alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Alert Dialog")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .setIcon(R.drawable.ball)
                .create();
    }

}