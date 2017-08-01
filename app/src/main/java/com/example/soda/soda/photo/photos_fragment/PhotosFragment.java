package com.example.soda.soda.photo.photos_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soda.soda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotosFragment extends Fragment {


    public PhotosFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.recyclerview_photos_fragment)
    RecyclerView mRecyclerView;

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this,root);
        return root;
    }

}
