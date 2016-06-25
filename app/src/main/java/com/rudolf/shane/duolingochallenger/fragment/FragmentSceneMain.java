package com.rudolf.shane.duolingochallenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rudolf.shane.duolingochallenger.R;
import com.rudolf.shane.duolingochallenger.base.BaseFragment;
import com.rudolf.shane.duolingochallenger.model.GameDisplayResponseModel;
import com.rudolf.shane.duolingochallenger.utils.Constants;
import com.rudolf.shane.duolingochallenger.volley.VolleyHelper;

import java.util.ArrayList;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentSceneMain extends BaseFragment{

    private ArrayList<GameDisplayResponseModel> gameDataArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GAME_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String lines[] = response.split("\\r?\\n");//split by new line
                gameDataArrayList.clear();
                for (String l: lines) {
                    GameDisplayResponseModel model = new Gson().fromJson(l, GameDisplayResponseModel.class);
                    Log.e("shaneTest", "l=" + l);
                    gameDataArrayList.add(model);
                }

                FragmentGamePlayMain fragment = FragmentGamePlayMain.create(gameDataArrayList.get(0).characterGrid);
                fragment.setOnWordSelectedListen(new FragmentGamePlayMain.OnWordSelectedListener() {
                    @Override
                    public boolean onWordSelected(String word) {
                        for (String correctWord: gameDataArrayList.get(0).wordLocations.values()) {
                            if (word.equals(correctWord)) return true;
                        }
                        return false;
                    }
                });

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("shaneTest", "error1 = " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestToCancelOnDestroy.add(stringRequest);
        VolleyHelper.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scene_main, container, false);

        return rootView;
    }
}
