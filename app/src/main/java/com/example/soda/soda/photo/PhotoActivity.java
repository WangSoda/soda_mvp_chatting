package com.example.soda.soda.photo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.soda.soda.R;
import com.example.soda.soda.photo.photos_fragment.PhotosFragment;
import com.example.soda.soda.util.ActivityUtils;

public class PhotoActivity extends AppCompatActivity {

    PhotosFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initFragment();
    }

    private void initFragment() {
        mFragment = (PhotosFragment) getSupportFragmentManager().findFragmentById(R.id.photo_content_fragment_photos_activity);
        if (mFragment == null){
            mFragment = new PhotosFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mFragment,R.id.photo_content_fragment_photos_activity);
        }
    }
}
