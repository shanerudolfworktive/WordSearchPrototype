package com.rudolf.shane.duolingochallenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudolf.shane.duolingochallenger.R;
import com.rudolf.shane.duolingochallenger.base.BaseFragment;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentGamePlayMain extends BaseFragment{

    String[][] gamePadData = {{"1", "2", "3"}, {"4","5","6"}};

    //clone constructor
    public static FragmentGamePlayMain create(String[][] gamePadData){
        FragmentGamePlayMain fragment = new FragmentGamePlayMain();
        fragment.gamePadData = gamePadData;//small data just cached in memory instead putting in bundle
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_play_main, container, false);

        for (int i = 0; i < gamePadData.length; i++) {

            LinearLayout row = new LinearLayout(getActivity());
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

            for (int j = 0; j < gamePadData[0].length; j++ ){
                TextView textView = new TextView(getActivity());
                textView.setText(gamePadData[i][j]);
                textView.setGravity(Gravity.CENTER);

                textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
                row.addView(textView);
            }
            rootView.addView(row);
        }

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("shaneTest", "test");
                return true;
            }
        });
        return rootView;
    }



}
