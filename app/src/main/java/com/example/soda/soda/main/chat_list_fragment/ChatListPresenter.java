package com.example.soda.soda.main.chat_list_fragment;

import android.util.Log;

import com.example.soda.soda.data.DataSourceResponse;
import com.example.soda.soda.data.remote.RemoteDataSourceCallback;
import com.example.soda.soda.util.MessageUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/7/29.
 */

public class ChatListPresenter implements ChatListContract.Presenter, EMMessageListener, RemoteDataSourceCallback.GetConversationsCallBack {
    private DataSourceResponse mDataSourceModel;
    private ChatListContract.View mView;
    public ChatListPresenter(DataSourceResponse dataSourceResponse, ChatListFragment mChatListView) {
        mDataSourceModel = dataSourceResponse;
        mView = mChatListView;
    }

    @Override
    public void start() {
        //设置消息监听
        mDataSourceModel.addMessageGetListener(this);
        //获取所有会话
        {
            mDataSourceModel.getAllConversations(this);
        }
    }

    @Override
    public void stop() {
        mDataSourceModel.remoteMessageGetListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        //收到消息
        Log.d("ReceiveMessage","收到消息");
        readMessages(list);
//        for (int i = 0; i < list.size(); i++) {
//            int size = list.size();
//            Log.d("ReceiveMessage","消息list大小 = " + size);
//            EMMessage message = list.get(i);
//
//            String username = null;
//            if (message.getChatType() == EMMessage.ChatType.GroupChat
//                    || message.getChatType() == EMMessage.ChatType.ChatRoom){
//                username = message.getTo();
//                Log.d("ReceiveMessage","接收来自" + username + "的消息");
//            }else {
//                username = message.getFrom();
//                Log.d("ReceiveMessage","ger from " + username + "的消息");
//            }
//
//            switch (message.getType()){
//                case TXT:
//                    Log.d("ReceiveMessage","获得文字消息");
//                    String string = ((EMTextMessageBody) message.getBody()).toString();
//                    Log.d("ReceiveMessage","消息内容" + string);
//                    break;
//                case IMAGE:
//                    Log.d("ReceiveMessage","获得图片消息");
//                    EMImageMessageBody body = (EMImageMessageBody) message.getBody();
//                    String thumbnailUrl = body.getThumbnailUrl();
//                    Log.d("ReceiveMessage","图片消息 url = " + thumbnailUrl);
//                    break;
//                case VOICE:
//                    Log.d("ReceiveMessage","获得语音消息");
//                    break;
//                case LOCATION:
//                    Log.d("ReceiveMessage","获得位置消息");
//                    break;
//                case VIDEO:
//                    break;
//                case FILE:
//                    break;
//            }
//
//        }
    }

    public void readMessages(List<EMMessage> list){
        for (int i = 0; i < list.size(); i++) {
            EMMessage message = list.get(i);

            String fromUserId = null;
            if (message.getChatType() == EMMessage.ChatType.GroupChat
                    || message.getChatType() == EMMessage.ChatType.ChatRoom){
                fromUserId = message.getTo();
                Log.d("ReceiveMessage","接收来自" + fromUserId + "的消息");
            }else {
                fromUserId = message.getFrom();
                Log.d("ReceiveMessage","ger from " + fromUserId + "的消息");
            }
            switch (message.getChatType()){
                case Chat:
                    long l = message.localTime();
                    long getcurrentTime = System.currentTimeMillis();
                    long msgTime = message.getMsgTime();//msgTime/1000为秒
                    switch (message.getType()){
                        case TXT:
                            String txtContent = MessageUtils.getMessageTxtContent(message);
                            fromUserId = MessageUtils.readMessageFrom(message);
                            Log.e("MessageContent","content ===" + txtContent);
                            mView.OnGetTxtMessage(fromUserId,txtContent,msgTime);
                            break;
                        case IMAGE:
                            fromUserId = MessageUtils.readMessageFrom(message);
                            String thumbnailUrl = ((EMImageMessageBody) message.getBody()).getThumbnailUrl();

                            mView.OnGetMediaMessage(fromUserId,msgTime, EMMessage.Type.IMAGE);
                            break;
                    }
                    break;
                case GroupChat:
                    return;
                case ChatRoom:
                    return;
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        //收到透传消息
        Log.d("ReceiveMessage","收到透传消息");
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {
        //收到已读回执
        Log.d("ReceiveMessage","收到已读回执");
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {
        //收到已送达回执
        Log.d("ReceiveMessage","收到已送达回执");
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
        //消息状态变动
        Log.d("ReceiveMessage","消息状态变动");
    }


    @Override
    public void onGetConversationsSuccess(Map<String, EMConversation> conversationMap) {
        mView.upDateChatList(conversationMap);
    }

    @Override
    public void onGetConversationsFailure() {

    }

    @Override
    public void onUserClickItem(String toUserId) {
        mView.startChattingActivity(toUserId);
    }
}
