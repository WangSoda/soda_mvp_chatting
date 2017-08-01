package com.example.soda.soda.main.friends_list_fragment;

import com.example.soda.soda.data.DataSourceResponse;
import com.example.soda.soda.data.remote.RemoteDataSourceCallback;

import java.util.List;

/**
 * Created by soda on 2017/7/29.
 */

public class FriendsListPresnter implements FriendsListContract.Presenter ,
        RemoteDataSourceCallback.GetFriendsListCallBack{
    private FriendsListContract.View mView;
    private DataSourceResponse mDataModel;

    public FriendsListPresnter(DataSourceResponse dataSourceResponse, FriendsListFragment mFriendsView) {
        mDataModel = dataSourceResponse;
        mView = mFriendsView;
    }

    @Override
    public void start() {
        if (mDataModel != null){
            mDataModel.getFriendsList(this);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void onGetFriendsListSuccess(List<String> friends) {
        mView.updateFriendsList(friends);
    }

    @Override
    public void onGetFriendsListFailure(int errorCode) {

    }
}
