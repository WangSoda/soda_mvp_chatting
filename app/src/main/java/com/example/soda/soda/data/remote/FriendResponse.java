package com.example.soda.soda.data.remote;

import android.util.Log;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by soda on 2017/7/27.
 */

public class FriendResponse {
    /**
     * 获得好友列表
     * 该方法可能出现异常，因此应该定义一个回调接口进行回传
     */
    public static void getAllFriends(final RemoteDataSourceCallback.GetFriendsListCallBack callBack){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("tryToGetFriends","to try");
                    List<String> friends = null;
                    friends = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.d("tryToGetFriends","onGetFriendsListSuccess");
                    callBack.onGetFriendsListSuccess(friends);
                } catch (HyphenateException e) {
                    Log.d("tryToGetFriends","onfailse" + e.getErrorCode());
                    callBack.onGetFriendsListFailure(e.getErrorCode());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 添加好友
     * @param toUserName
     * @param reason
     */
    public static void addFriend(String toUserName,String reason){
        try {
            EMClient.getInstance().contactManager().addContact(toUserName,reason);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同意添加好友
     * @param toUserName
     */
    public static void acceptFriend(String toUserName){
        try {
            EMClient.getInstance().contactManager().acceptInvitation(toUserName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拒绝好友请求
     * @param toUserName
     */
    public static void declineFriend(String toUserName){
        try {
            EMClient.getInstance().contactManager().declineInvitation(toUserName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除好友
     * @param toUserName
     */
    public static void deleteFriend(String toUserName){
        try {
            EMClient.getInstance().contactManager().deleteContact(toUserName);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置好友状态的监听
     * 包括   被同意 被拒绝 收到邀请    被删除 增加了联系人
     * @param friendStatusListener
     */
    public static void setFriendStatusListener(EMContactListener friendStatusListener){
        EMClient.getInstance().contactManager().setContactListener(friendStatusListener);
    }
}
