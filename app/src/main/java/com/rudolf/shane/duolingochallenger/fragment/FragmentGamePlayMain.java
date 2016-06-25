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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentGamePlayMain extends BaseFragment{
    ArrayList<ArrayList<String>> gamePadData;
//    HashMap<Coor, TextView> coorToTextViewMap = new HashMap<>();
//    HashMap<TextView, Coor> textViewToCoorMap = new HashMap<>();
    HashMap<Coor, CoordinatedTextView> coordinatedTextViewMap = new HashMap<>();


    OnWordSelectedListener onWordSelectedListener;
    LinkedHashSet<CoordinatedTextView> selectedCoordinatedTextViewSet = new LinkedHashSet<>();
    HashSet<CoordinatedTextView> correctCoordinatedTextViewSet = new HashSet<>();

    //clone constructor
    public static FragmentGamePlayMain create(ArrayList<ArrayList<String>> gamePadData){
        FragmentGamePlayMain fragment = new FragmentGamePlayMain();
        fragment.gamePadData = gamePadData;//small data just cached in memory instead putting in bundle
        return fragment;
    }

    public void setOnWordSelectedListen(OnWordSelectedListener onWordSelectedListener){
        this.onWordSelectedListener = onWordSelectedListener;
    }

    public void highLightCell(int x, int y){
        CoordinatedTextView coordinatedTextView = coordinatedTextViewMap.get(new Coor(x, y));
        if (coordinatedTextView == null) return;

        coordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
        correctCoordinatedTextViewSet.add(coordinatedTextView);
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
                Coor coor = new Coor(j, i);
                coordinatedTextViewMap.put(coor, new CoordinatedTextView(textView, coor));
            }
            rootView.addView(row);
        }

        //recover orientation during rotation;
        for (CoordinatedTextView coordinatedTextView: correctCoordinatedTextViewSet) coordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);

        rootView.setOnTouchListener(new View.OnTouchListener() {
            Point startPoint;
            CoordinatedTextView startCoordinatedTextView;
            Coor startCoor;
            private void clearSelectedView(){
                for (Iterator<CoordinatedTextView> iterator = selectedCoordinatedTextViewSet.iterator(); iterator.hasNext();) {
                    CoordinatedTextView coordinatedTextView = iterator.next();
                    if (!correctCoordinatedTextViewSet.contains(coordinatedTextView)) coordinatedTextView.textView.setBackgroundResource(R.drawable.drawable_game_button_background);
                    iterator.remove();
                }
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Point point = new Point(event.getRawX(), event.getRawY());

                if (event.getAction() == MotionEvent.ACTION_DOWN && getIntersectedTextView(point) != null){
                    startPoint = point;
                    startCoordinatedTextView = getIntersectedTextView(point);
                    startCoor = startCoordinatedTextView.coor;
                    startCoordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
                }else if (event.getAction()==MotionEvent.ACTION_UP){
                    StringBuilder sb= new StringBuilder();
                    for (CoordinatedTextView coordinatedTextView: selectedCoordinatedTextViewSet) {
                        sb.append(coordinatedTextView.textView.getText().toString());
                    }
                    Log.e("shaneTest", sb.toString());
                    if(onWordSelectedListener!=null) {
                        if (onWordSelectedListener.onWordSelected(sb.toString())){
                            for (CoordinatedTextView coordinatedTextView: selectedCoordinatedTextViewSet) correctCoordinatedTextViewSet.add(coordinatedTextView);
                        }
                    }
                    clearSelectedView();
                }else if (event.getAction() == MotionEvent.ACTION_MOVE && getIntersectedTextView(point) != null){
                    CoordinatedTextView endPointCoordinatedTextView = getIntersectedTextView(point);
                    Coor endCoor = endPointCoordinatedTextView.coor;
                    if (endCoor.x == startCoor.x){//highlight vertical
                        clearSelectedView();
                        for (int y = Math.min(startCoor.y, endCoor.y); y<=Math.max(startCoor.y, endCoor.y); y++) {
                            CoordinatedTextView selectedCoordinatedTextView = coordinatedTextViewMap.get(new Coor(startCoor.x, y));
                            selectedCoordinatedTextViewSet.add(selectedCoordinatedTextView);
                            selectedCoordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (endCoor.y == startCoor.y){//highlight horizontal
                        clearSelectedView();
                        for (int x = Math.min(startCoor.x, endCoor.x); x<=Math.max(startCoor.x, endCoor.x); x++) {
                            CoordinatedTextView selectedCoordinatedTextView = coordinatedTextViewMap.get(new Coor(x, startCoor.y));
                            selectedCoordinatedTextViewSet.add(selectedCoordinatedTextView);
                            selectedCoordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (((float)startCoor.y - (float)endCoor.y)/((float)startCoor.x - (float)endCoor.x) == 1f){//highlight positive slope
                        clearSelectedView();
                        int startX = Math.min(startCoor.x, endCoor.x);
                        int startY = Math.min(startCoor.y, endCoor.y);

                        while (startX<=Math.max(startCoor.x, endCoor.x)){
                            CoordinatedTextView selectedCoordinatedTextView = coordinatedTextViewMap.get(new Coor(startX++, startY++));
                            selectedCoordinatedTextViewSet.add(selectedCoordinatedTextView);
                            selectedCoordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }else if (((float)startCoor.y - (float)endCoor.y)/((float)startCoor.x - (float)endCoor.x) == -1f){//highlight negative slope
                        clearSelectedView();
                        int startX = Math.max(startCoor.x, endCoor.x);
                        int startY = Math.min(startCoor.y, endCoor.y);
                        while (startX>=Math.min(startCoor.x, endCoor.x)){
                            CoordinatedTextView selectedCoordinatedTextView = coordinatedTextViewMap.get(new Coor(startX--, startY++));
                            selectedCoordinatedTextViewSet.add(selectedCoordinatedTextView);
                            selectedCoordinatedTextView.textView.setBackgroundResource(R.color.colorPrimaryDark);
                        }
                    }
                }
                return true;
            }
        });
        return rootView;
    }

    protected CoordinatedTextView getIntersectedTextView(Point point){
        for (CoordinatedTextView coordinatedTextView: coordinatedTextViewMap.values()) {
            if (isPointInsideView(point, coordinatedTextView.textView)) return coordinatedTextView;
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

    public interface OnWordSelectedListener{
        boolean onWordSelected(String word);
    }

    private class CoordinatedTextView{
        TextView textView;
        Coor coor;

        public CoordinatedTextView(TextView textView, int x, int y){
            this.textView = textView;
            this.coor = new Coor(x,y);
        }

        public CoordinatedTextView(TextView textView, Coor coor){
            this.textView = textView;
            this.coor = coor;
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
