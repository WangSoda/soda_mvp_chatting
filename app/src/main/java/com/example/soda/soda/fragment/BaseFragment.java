package com.example.soda.soda.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by soda on 2017/7/27.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("FragmentLife","onAttach" + this.getClass().getName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FragmentLife","onCreate" + this.getClass().getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("FragmentLife","onCreateView" + this.getClass().getName());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("FragmentLife","onActivityCreated" + this.getClass().getName());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FragmentLife","onResume" + this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("FragmentLife","onPause" + this.getClass().getName());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("FragmentLife","onStop" + this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("FragmentLife","onDestroyView" + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FragmentLife","onDestroy" + this.getClass().getName());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("FragmentLife","onDetach" + this.getClass().getName());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("FragmentLife","setUserVisibleHint"
                + this.getClass().getName() + isVisibleToUser);
    }
}
