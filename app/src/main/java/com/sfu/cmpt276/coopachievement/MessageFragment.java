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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.sfu.cmpt276.coopachievement.model.Singleton;

import java.util.ArrayList;


/*
 * Message Fragment Class: Display an alert dialog with override methods to display with sound,
 * Animation and Images
 */

public class MessageFragment extends AppCompatDialogFragment {
    private ArrayAdapter themeAdapter;
    private Spinner themeSpinner;
    private ImageView dialogImageView;
    private TextView achievementTextView;
    private TextView achievementName;
    private int currentScore;
    private ArrayList<Integer> thresholdScores;
    private int achievementIndex;
    private int nextAchievementScore;
    private int pointsToNext = 0;
    private boolean maxScore;
    private Singleton instance;
    private int themeIndex;
    ;
    private TextView nextScoreView;
    private String[] achievementList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create view by loading layout
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.congratulation_layout, null);

        instance = Singleton.getInstance();
        themeIndex = instance.getThemeIndex();
        thresholdScores = this.getArguments().getIntegerArrayList("thresholds");
        dialogImageView = (ImageView) v.findViewById(R.id.image_alertDialog);

        //Make spinner and set the selection to default
        themeSpinner = v.findViewById(R.id.alert_spinner);
        themeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.theme_choice,
                android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);
        themeSpinner.setSelection(themeIndex);

        spinnerClickCallback();

        MessageFragment.this.setCancelable(false);

        changeThemeImage(themeIndex);


        achievementTextView = (TextView) v.findViewById(R.id.achievement_name);
        achievementName = (TextView) getActivity().findViewById(R.id.displayAchievementText);

        currentScore = this.getArguments().getInt("score");

        achievementList = populateAchievementList(themeIndex);

        //set achievement gotten inside new game activity
        String txtAchievementName = achievementName.getText().toString();
        achievementTextView.setText(txtAchievementName);

        achievementIndex = 0;
        while (!achievementList[achievementIndex].equals(txtAchievementName)) {
            achievementIndex++;
        }

        maxScore = false;
        int numPlayers = getArguments().getInt("numPlayers");
        if (achievementIndex != thresholdScores.size() - 1) {
            nextAchievementScore = thresholdScores.get(achievementIndex + 1) * numPlayers;
            pointsToNext = nextAchievementScore - currentScore;
        } else {
            maxScore = true;
        }


        nextScoreView = (TextView) v.findViewById(R.id.txt_points_to_next);
        setNextAchievementText(achievementList);
        //create animation for theme
        runAnimation(dialogImageView);

        Button btn = v.findViewById(R.id.replay_animation_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runAnimation(dialogImageView);
            }
        });
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
                .setPositiveButton(android.R.string.ok, listener)
                .setIcon(R.drawable.ball)
                .create();
    }

    private void setNextAchievementText(String[] achievementList) {
        if (maxScore) {
            nextScoreView.setText(R.string.alert_max_score_achieved);
        } else {
            nextScoreView.setText(getString(R.string.alert_not_max_achievement_1) + pointsToNext +
                    getString(R.string.alert_not_max_achievement_2) +
                    achievementList[achievementIndex + 1]);
        }
    }

    private void changeThemeImage(int themeIndex) {
        if (themeIndex == 0) {
            dialogImageView.setImageResource(R.drawable.leviathan);
        }
        if (themeIndex == 1) {
            dialogImageView.setImageResource(R.drawable.pawpatrol);
        }
        if (themeIndex == 2) {
            dialogImageView.setImageResource(R.drawable.dinosaur);
        }
    }

    private void runAnimation(ImageView dialogImageView) {
        Animation rotateLoop;
        rotateLoop = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_loop);
        dialogImageView.startAnimation(rotateLoop);
    }

    private void spinnerClickCallback() {
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String theme = themeSpinner.getSelectedItem().toString();
                int parentID = adapterView.getId();
                if (parentID == R.id.alert_spinner) {
                    switch (theme) {
                        case "Mythical":
                            themeIndex = 0;
                            spinnerUpdate();
                            break;
                        case "Paw Patrol":
                            themeIndex = 1;
                            spinnerUpdate();
                            break;
                        case "Dinosaur":
                            themeIndex = 2;
                            spinnerUpdate();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void spinnerUpdate() {
        instance.setThemeIndex(themeIndex);
        achievementList = populateAchievementList(themeIndex);
        setNextAchievementText(achievementList);
        achievementTextView.setText(achievementList[achievementIndex]);
        changeThemeImage(themeIndex);
        runAnimation(dialogImageView);
    }

    private String[] populateAchievementList(int themeIndex) {

        if (themeIndex == 0) {
            String[] themeArray = getResources().getStringArray(R.array.mythical);
            return themeArray;
        }
        if (themeIndex == 1) {
            String[] themeArray = getResources().getStringArray(R.array.paw_patrol);
            return themeArray;

        }
        String[] themeArray = getResources().getStringArray(R.array.dinosaur);
        return themeArray;
    }

}

