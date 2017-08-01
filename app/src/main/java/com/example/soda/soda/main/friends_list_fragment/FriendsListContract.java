package com.example.soda.soda.main.friends_list_fragment;

import com.example.soda.soda.BasePresenter;
import com.example.soda.soda.BaseView;

import java.util.List;

/**
 * Created by soda on 2017/7/29.
 */

public interface FriendsListContract {
    interface Presenter extends BasePresenter{}
    interface View extends BaseView<Presenter>{

        void updateFriendsList(List<String> friends);
    }
}
