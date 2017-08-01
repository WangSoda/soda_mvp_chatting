package com.example.soda.soda.data.remote;

import com.hyphenate.chat.EMConversation;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/7/29.
 */

public interface RemoteDataSourceCallback {
    /**
     *
     */
    interface GetFriendsListCallBack{
        /**
         * 从环信获取联系人数据的回调方法，因为此线程工作在子线程，所以在更新ui时千万要注意
         * getActivity().runOnUiThread
         * @param friends 联系人id列表
         */
        void onGetFriendsListSuccess(List<String> friends);
        void onGetFriendsListFailure(int errorCode);
    }

    interface GetConversationsCallBack{
        void onGetConversationsSuccess(Map<String, EMConversation> conversationMap);
        void onGetConversationsFailure();
    }
}
