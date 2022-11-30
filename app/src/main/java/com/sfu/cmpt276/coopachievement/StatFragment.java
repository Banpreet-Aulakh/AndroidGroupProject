package com.sfu.cmpt276.coopachievement;

import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class StatFragment extends DialogFragment {
    int[] nameTextViews = {R.id.text_achivement1, R.id.text_achievement2, R.id.text_achievement3,
            R.id.text_achievement4, R.id.text_achievement5, R.id.text_achievement6,
            R.id.text_achievement7, R.id.text_achievement8, R.id.text_achievement9};
    int[] countTextViews = {R.id.text_count1, R.id.text_count2, R.id.text_count3,
            R.id.text_count4, R.id.text_count5, R.id.text_count6,
            R.id.text_count7, R.id.text_count8, R.id.text_count9};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] achievementList= getArguments().getStringArray("achievementList");
        int[] counter = getArguments().getIntArray("counter");
        View v = getLayoutInflater().inflate(R.layout.stat_fragment, container, false);

        for(int i = 0; i < counter.length; i++){
            TextView nameText = v.findViewById(nameTextViews[i]);
            TextView countText = v.findViewById(countTextViews[i]);

            nameText.setText(achievementList[i]+"");
            countText.setText(counter[i]+"");
        }

        return v;
    }

    @Override
    public void onResume() {

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();;
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();
    }
}
