package com.sfu.cmpt276.coopachievement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

    @Override
    public void onResume() {
        //Source: https://gist.github.com/marc0x71/f571f4dfe328b662860cd4e80bd7f6af
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();;
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }

}
