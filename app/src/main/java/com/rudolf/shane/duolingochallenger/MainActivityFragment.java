package com.rudolf.shane.duolingochallenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rudolf.shane.duolingochallenger.base.BaseFragment;
import com.rudolf.shane.duolingochallenger.fragment.FragmentSceneMain;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new FragmentSceneMain()).commit();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
