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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sfu.cmpt276.coopachievement.model.Singleton;

import org.w3c.dom.Text;

import java.util.ArrayList;


/*
 * Message Fragment Class: Display an alert dialog with override methods to display with sound,
 * Animation and Images
 */

public class MessageFragment extends AppCompatDialogFragment {
    private ArrayAdapter themeAdapter;
    private Spinner themeSpinner;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view by loading layout
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.congratulation_layout,null);

        Singleton instance = Singleton.getInstance();
        int themeIndex = instance.getThemeIndex();
        ImageView dialogImageView= (ImageView) v.findViewById(R.id.image_alertDialog);

        themeSpinner = v.findViewById(R.id.alert_spinner);
        themeAdapter= ArrayAdapter.createFromResource(getActivity(), R.array.theme_choice,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        MessageFragment.this.setCancelable(false);

        if (themeIndex==0){
            dialogImageView.setImageResource(R.drawable.leviathan);
        }
        if (themeIndex==1){
            dialogImageView.setImageResource(R.drawable.pawpatrol);
        }
        if (themeIndex==2){
            dialogImageView.setImageResource(R.drawable.dinosaur);
        }


        TextView tv =(TextView) v.findViewById(R.id.achievement_name);
        TextView achievementName = (TextView) getActivity().findViewById(R.id.displayAchievementText);

        final int currentScore= this.getArguments().getInt("score");
        String[] achievementList = populateAchievementList(themeIndex);
        final ArrayList<Integer> thresholdScores = this.getArguments().getIntegerArrayList("thresholds");
        final String[] achievementsList;

        //set achievement gotten inside new game activity
        String txtAchievementName= achievementName.getText().toString();
        tv.setText(txtAchievementName);

        int achievementIndex = 0;
        while(!achievementList[achievementIndex].equals(txtAchievementName)){
            achievementIndex++;
        }

        int nextAchievementScore;
        int pointsToNext = 0;
        boolean maxScore = false;
        int numPlayers = getArguments().getInt("numPlayers");
        if (achievementIndex != thresholdScores.size() - 1){
            nextAchievementScore = thresholdScores.get(achievementIndex + 1)  * numPlayers;
            pointsToNext = nextAchievementScore - currentScore;
        }
        else {
            maxScore = true;
        }


        TextView nextScoreView = (TextView) v.findViewById(R.id.txt_points_to_next);
        if (maxScore) {
            nextScoreView.setText(R.string.alert_max_score_achieved);
        }
        else {
            nextScoreView.setText(getString(R.string.alert_not_max_achievement_1) + pointsToNext +
                    getString(R.string.alert_not_max_achievement_2) +
                    achievementList[achievementIndex + 1]);
        }
        //create animation for theme
        Animation rotateLoop;
        rotateLoop=AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_loop);
        dialogImageView.startAnimation(rotateLoop);


        //create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //create dialog when clicking save and finish the activity when clicking ok
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

    private String[] populateAchievementList(int themeIndex) {

        if(themeIndex== 0){
            String[] themeArray=getResources().getStringArray(R.array.mythical);
            return themeArray;
        }
        if (themeIndex==1)
        {
            String[] themeArray=getResources().getStringArray(R.array.paw_patrol);
            return themeArray;

        }
        String[] themeArray = getResources().getStringArray(R.array.dinosaur);
        return themeArray;
    }
}

