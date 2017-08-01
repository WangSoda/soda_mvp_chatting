package com.example.soda.soda.photo.photos_fragment;

import com.example.soda.soda.BasePresenter;
import com.example.soda.soda.BaseView;

/**
 * Created by soda on 2017/7/31.
 */

public interface PhotosContract {
    interface Presenter extends BasePresenter{}
    interface View extends BaseView<Presenter>{}
}
