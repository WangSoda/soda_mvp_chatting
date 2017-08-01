package com.example.soda.soda.util;

import android.util.Log;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

/**
 * Created by soda on 2017/7/30.
 */

public class MessageUtils {
    public static String readMessageFrom(EMMessage message){
        String userId = null;
        if (message.getChatType() == EMMessage.ChatType.GroupChat
                || message.getChatType() == EMMessage.ChatType.ChatRoom){
            userId = message.getTo();
            Log.d("ReceiveMessage","接收来自" + userId + "的消息");
        }else {
            userId = message.getFrom();
            Log.d("ReceiveMessage","ger from " + userId + "的消息");
        }
        return userId;
    }

    /**
     * 在判断Message为文字消息后调用该语句获取文字内容
     * @param message
     * @return
     */
    public static String getMessageTxtContent(EMMessage message){
        return ((EMTextMessageBody)message.getBody()).getMessage();
    }
    public static void readMessage(List<EMMessage> list){
        for (int i = 0; i < list.size(); i++) {
            EMMessage message = list.get(i);
            switch (message.getType()){
                case TXT:
                    Log.d("ReceiveMessage","获得文字消息");
                    String string = message.getBody().toString();
                    Log.d("ReceiveMessage","消息内容" + string);
                    break;
                case IMAGE:
                    Log.d("ReceiveMessage","获得图片消息");
                    EMImageMessageBody body = (EMImageMessageBody) message.getBody();
                    String thumbnailUrl = body.getThumbnailUrl();
                    Log.d("ReceiveMessage","图片消息 url = " + thumbnailUrl);
                    break;
                case VOICE:
                    Log.d("ReceiveMessage","获得语音消息");
                    break;
                case LOCATION:
                    Log.d("ReceiveMessage","获得位置消息");
                    break;
                case VIDEO:
                    break;
                case FILE:
                    break;
            }
        }
    }
}
