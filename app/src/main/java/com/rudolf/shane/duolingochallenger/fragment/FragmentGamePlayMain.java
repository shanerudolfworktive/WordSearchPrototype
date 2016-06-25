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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentGamePlayMain extends BaseFragment{

    ArrayList<ArrayList<String>> gamePadData;

    HashMap<Coor, TextView> coorToTextViewMap = new HashMap<>();
    HashMap<TextView, Coor> textViewToCoorMap = new HashMap<>();

    //clone constructor
    public static FragmentGamePlayMain create(ArrayList<ArrayList<String>> gamePadData){
        FragmentGamePlayMain fragment = new FragmentGamePlayMain();
        fragment.gamePadData = gamePadData;//small data just cached in memory instead putting in bundle
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_play_main, container, false);

        for (int i = 0; i < gamePadData.size(); i++) {

            LinearLayout row = new LinearLayout(getActivity());
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f));

            for (int j = 0; j < gamePadData.get(0).size(); j++ ){
                TextView textView = (TextView) inflater.inflate(R.layout.text_view_game_pad, rootView, false);
                textView.setText(gamePadData.get(i).get(j));
                row.addView(textView);
                Coor coor = new Coor(i, j);
                coorToTextViewMap.put(coor, textView);
                textViewToCoorMap.put(textView,coor);
            }
            rootView.addView(row);
        }

        rootView.setOnTouchListener(new View.OnTouchListener() {
            Point startPoint;
            TextView startTextView;
            Coor startCoor;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point(event.getRawX(), event.getRawY());

                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    startPoint = point;
                    startTextView = getIntersectedTextView(point);
                    startCoor = textViewToCoorMap.get(startTextView);
                    startTextView.setBackgroundResource(R.color.colorPrimaryDark);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    clearSelection();
                }else if (event.getAction() == MotionEvent.ACTION_MOVE){
                    TextView endPointTextView = getIntersectedTextView(point);
                    Coor endCoor = textViewToCoorMap.get(endPointTextView);
                    if (endCoor.x == startCoor.x){//highlight vertical
                        clearSelection();
                        for (int y = Math.min(startCoor.y, endCoor.y); y<=Math.max(startCoor.y, endCoor.y); y++) {
                            coorToTextViewMap.get(new Coor(startCoor.x, y)).setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (endCoor.y == startCoor.y){//highlight horizontal
                        clearSelection();
                        for (int x = Math.min(startCoor.x, endCoor.x); x<=Math.max(startCoor.x, endCoor.x); x++) {
                            coorToTextViewMap.get(new Coor(x, startCoor.y)).setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (((float)startCoor.y - (float)endCoor.y)/((float)startCoor.x - (float)endCoor.x) == 1f){//highlight positive slope
                        clearSelection();
                        int startX = Math.min(startCoor.x, endCoor.x);
                        int startY = Math.min(startCoor.y, endCoor.y);

                        while (startX<=Math.max(startCoor.x, endCoor.x)){
                            coorToTextViewMap.get(new Coor(startX++, startY++)).setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (((float)startCoor.y - (float)endCoor.y)/((float)startCoor.x - (float)endCoor.x) == -1f){//highlight negative slope
                        clearSelection();
                        int startX = Math.max(startCoor.x, endCoor.x);
                        int startY = Math.min(startCoor.y, endCoor.y);
                        while (startX>=Math.min(startCoor.x, endCoor.x)){
                            coorToTextViewMap.get(new Coor(startX--, startY++)).setBackgroundResource(R.color.colorPrimaryDark);
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

        @Override
        public String toString() {
            return "x=" + x + ";y=" + y;
        }
    }
}
