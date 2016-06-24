package com.rudolf.shane.duolingochallenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudolf.shane.duolingochallenger.R;
import com.rudolf.shane.duolingochallenger.base.BaseFragment;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentGamePlayMain extends BaseFragment{

    String[][] gamePadData = {{"1", "2", "3", "4", "5", "6", "7"}, {"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"},{"1", "2", "3", "4", "5", "6", "7"}};

    HashMap<Coor, TextView> coorToTextViewMap = new HashMap<>();
    HashMap<TextView, Coor> textViewToCoorMap = new HashMap<>();

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
                Coor coor = new Coor(i, j);
                coorToTextViewMap.put(coor, textView);
                textViewToCoorMap.put(textView,coor);
            }
            rootView.addView(row);
        }

        rootView.setOnTouchListener(new View.OnTouchListener() {
            Point touchDownPoint;
            TextView touchDownTextView;
            Coor touchDownCoor;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point(event.getRawX(), event.getRawY());

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    touchDownPoint = point;
                    touchDownTextView = getIntersectedTextView(point);
                    touchDownCoor = textViewToCoorMap.get(touchDownTextView);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    clearSelection();
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    TextView endPointTextView = getIntersectedTextView(point);
                    if (endPointTextView == null || endPointTextView == touchDownTextView) return true;
                    Coor endCoor = textViewToCoorMap.get(endPointTextView);
                    Log.e("shaneTest", "size = " + textViewToCoorMap.size() + "; endPointTextView = " + endPointTextView + "; touchDownTextView = " + touchDownTextView);
                    if (textViewToCoorMap.get(endPointTextView).x == touchDownCoor.x){
                        clearSelection();
                        for (int y = Math.min(touchDownCoor.y, endCoor.y); y<=Math.max(touchDownCoor.y, endCoor.y); y++) {
                            coorToTextViewMap.get(new Coor(touchDownCoor.x, y)).setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (textViewToCoorMap.get(endPointTextView).y == touchDownCoor.y){
                        clearSelection();
                        for (int x = Math.min(touchDownCoor.x, endCoor.x); x<=Math.max(touchDownCoor.x, endCoor.x); x++) {
                            coorToTextViewMap.get(new Coor(x, touchDownCoor.y)).setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }
                }

                return true;
            }
        });
        return rootView;
    }

    protected void clearSelection(){
        for (TextView textView: coorToTextViewMap.values()) {
            textView.setBackgroundResource(R.drawable.drawable_game_button_background);
        }
    }

    protected TextView getIntersectedTextView(Point point){
        for (TextView textView: coorToTextViewMap.values()) {
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
        public int y;
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
            return Arrays.hashCode(new int[]{x,y});
        }
    }
}
