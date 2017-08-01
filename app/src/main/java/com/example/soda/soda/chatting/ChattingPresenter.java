package com.example.soda.soda.chatting;

import android.util.Log;

import com.example.soda.soda.data.DataSourceResponse;
import com.example.soda.soda.util.AlertUtils;
import com.example.soda.soda.util.MessageUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by soda on 2017/7/30.
 */

public class ChattingPresenter implements ChattingContract.Presenter, EMMessageListener {
    private DataSourceResponse mDataModel;
    private ChattingContract.View mView;


    public ChattingPresenter(DataSourceResponse dataModel, ChattingFragment mFragment) {
        mDataModel = dataModel;
        mView =  mFragment;
    }

    @Override
    public void start() {
        mDataModel.addMessageGetListener(this);
        {
            mDataModel.gettoUsersAllMessage(mView.getmChattingToUserId(),this);
        }
    }

    @Override
    public void stop() {
        mDataModel.remoteMessageGetListener(this);
    }

    public void SendMessage(String toUserId, String inputContent, EMMessage.Type messageType) {
        switch (messageType){
            case TXT:
                mDataModel.sendTextMessage(inputContent,toUserId);
                long currentTimeMillis = System.currentTimeMillis();
                mView.onSendMessage(toUserId,inputContent,currentTimeMillis);
                break;
            case IMAGE:
                break;
            case VIDEO:
                break;
            case LOCATION:
                break;
            case VOICE:
                break;
            case FILE:
                break;
            case CMD:
                break;
        }
    }

    /**
     * 当获取到消息时
     * 先要判断是不是当前聊天页面所关注的聊天内容
     * @param list
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        if (AlertUtils.canAlert()){
            AlertUtils.alertUser();
        }
        readMessages(list);

    }
    public void readMessages(List<EMMessage> list){
        for (int i = 0; i < list.size(); i++) {
            EMMessage message = list.get(i);

            String fromUserId = null;
            //当前页面只关心聊天信息
            switch (message.getChatType()){
                case Chat:
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

                            mView.OnGetImageMessage(fromUserId,thumbnailUrl,msgTime);
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

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
