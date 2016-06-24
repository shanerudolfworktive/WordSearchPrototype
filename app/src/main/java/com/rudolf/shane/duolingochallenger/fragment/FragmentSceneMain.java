package com.rudolf.shane.duolingochallenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rudolf.shane.duolingochallenger.base.BaseFragment;

/**
 * Created by shane on 6/24/16.
 */
public class FragmentSceneMain extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new FragmentGamePlayMain()).commit();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
