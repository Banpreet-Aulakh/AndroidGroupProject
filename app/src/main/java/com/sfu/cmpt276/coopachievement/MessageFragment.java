package com.sfu.cmpt276.coopachievement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;


/*
 * Message Fragment Class: Display congratulations message once player found all mines
 */

public class MessageFragment extends AppCompatDialogFragment {



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view by loading layout
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.congratulation_layout,null);

        final ImageView dialogImageView= (ImageView) v.findViewById(R.id.image_alertDialog);
        dialogImageView.setImageResource(R.drawable.alone);

        final TextView tv =(TextView) v.findViewById(R.id.achievement_name);
        tv.setText("AEEEEE");

        Animation rotateLoop;

        rotateLoop=AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_loop);


        dialogImageView.startAnimation(rotateLoop);


        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //create dialog when winning to for escaping game, or change setting
                        getActivity().finish();
                        break;
                }
            }
        };

        //Build Alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("YEEEET!")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .setIcon(R.drawable.ball)
                .create();
    }

}