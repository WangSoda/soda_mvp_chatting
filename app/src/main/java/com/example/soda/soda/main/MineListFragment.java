package com.example.soda.soda.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soda.soda.R;
import com.example.soda.soda.fragment.BaseFragment;

/**
 * Created by soda on 2017/7/27.
 */

public class MineListFragment extends BaseFragment{
    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_mine_list,container,false);
        return root;
    }
}
