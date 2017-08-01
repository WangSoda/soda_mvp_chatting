package com.example.soda.soda.data;


import com.example.soda.soda.data.remote.DataResponse;
import com.example.soda.soda.data.remote.FriendResponse;
import com.example.soda.soda.data.remote.RemoteDataSourceCallback;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMConversation;

import java.util.Map;

/**
 * Created by soda on 2017/7/29.
 */

public class DataSourceResponse {
    private static DataSourceResponse mInstance;
    public static DataSourceResponse getInstance(){
        if (mInstance == null){
            mInstance = new DataSourceResponse();
        }
        return mInstance;
    }
    private DataSourceResponse(){
        mDataResponse = new DataResponse();
    }
    //获取联系人列表
    public void getFriendsList(RemoteDataSourceCallback.GetFriendsListCallBack callBack){
        FriendResponse.getAllFriends(callBack);
    }

    private DataResponse mDataResponse;
    //添加消息监听
    public void addMessageGetListener(EMMessageListener emMessageListener){
        mDataResponse.addMessageGetListener(emMessageListener);
    }
    public void remoteMessageGetListener(EMMessageListener emMessageListener){
        mDataResponse.remoteMessageGetListener(emMessageListener);
    }
    //获取所有会话
    public void getAllConversations(final RemoteDataSourceCallback.GetConversationsCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, EMConversation> conversationMap = mDataResponse.getAllConversations();
                if (conversationMap.size() > 0){
                    callBack.onGetConversationsSuccess(conversationMap);
                }else {
                    callBack.onGetConversationsFailure();
                }
            }
        }).start();
    }

    public void gettoUsersAllMessage(String toUserName,EMMessageListener listener){
        mDataResponse.getAllMessages(toUserName, listener);
    }

    //发送文字消息
    public void sendTextMessage(String content,String toUserName){
        mDataResponse.sendTextMessage(content,toUserName);
    }
    //发送图片消息
    public void sendImageMessage(String filePath,boolean sendOriginalImage,String toUserName){
        mDataResponse.sendImageMessage(filePath,sendOriginalImage,toUserName);
    }
}
