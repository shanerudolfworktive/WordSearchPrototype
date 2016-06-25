package com.rudolf.shane.duolingochallenger.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.Request;

import java.util.ArrayList;

/**
 * Created by shane on 6/24/16.
 */
public abstract class BaseFragment extends Fragment{

    protected ArrayList<Request> requestToCancelOnDestroy= new ArrayList<>();
    public OnResumeListener onResumeListener;
    public interface OnResumeListener{
        void onResume();
    }

    public void setOnResumeListener(OnResumeListener onResumeListener){
        this.onResumeListener = onResumeListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);//default to retain fragment
    }

    @Override
    public void onResume() {
        super.onResume();
        if (onResumeListener != null) onResumeListener.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Request request: requestToCancelOnDestroy) request.cancel();

    }
}
