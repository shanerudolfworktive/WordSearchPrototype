package com.rudolf.shane.duolingochallenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudolf.shane.duolingochallenger.R;
import com.rudolf.shane.duolingochallenger.base.BaseFragment;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentGamePlayMain extends BaseFragment{

    String[][] gamePadData = {{"1", "2", "3", "4", "5", "6", "7"}, {"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"}};

    HashMap<Coor, TextView> gamePadTextViewMap = new HashMap<>();

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
                TextView textView = (TextView) inflater.inflate(R.layout.text_view_game_pad, rootView, false);
                textView.setText(gamePadData[i][j]);
                row.addView(textView);
                gamePadTextViewMap.put(new Coor(i, j), textView);
            }
            rootView.addView(row);
        }

        rootView.setOnTouchListener(new View.OnTouchListener() {
            Point touchDownPoint;
            TextView touchDownTextView;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point(event.getRawX(), event.getRawY());

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    touchDownPoint = point;
                    touchDownTextView = getIntersectedTextView(point);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    for (TextView textView: gamePadTextViewMap.values()) {
                        textView.setBackgroundResource(R.color.colorGreen);
                    }
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    for (TextView textView: gamePadTextViewMap.values()) {
                        if (isPointInsideView(point,textView)) {
                            textView.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }
                }

                return true;
            }
        });
        return rootView;
    }

    protected TextView getIntersectedTextView(Point point){
        for (TextView textView: gamePadTextViewMap.values()) {
            if (isPointInsideView(point, textView)) return textView;
        }
        return null;
    }

    protected boolean isPointInsideView(Point point, View view){
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        if(( point.x > viewX && point.x < (viewX + view.getWidth())) && ( point.y > viewY && point.y < (viewY + view.getHeight()))){
            return true;
        } else {
            return false;
        }
    }

    private class Point{
        public float x;
        public float y;

        public Point(float x, float y){
            this.x = x;
            this.y = y;
        }
    }

    private class Coor {
        public int x;
        public float y;
        public Coor(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Coor)) return false;
            Coor c = (Coor) o;
            return x == c.x && y == c.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x,y);
        }
    }
}
