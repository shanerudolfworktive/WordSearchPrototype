package com.rudolf.shane.duolingochallenger.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.rudolf.shane.duolingochallenger.R;

import java.util.ArrayList;

/**
 * Created by shane on 6/24/16.
 */
public abstract class BaseFragment extends Fragment{

    protected ArrayList<Request> requestToCancelOnDestroy= new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//default to retain fragment
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game_play_main, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Request request: requestToCancelOnDestroy) request.cancel();

    }
}
