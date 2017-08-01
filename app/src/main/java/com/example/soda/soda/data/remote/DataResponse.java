package com.example.soda.soda.data.remote;

import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/7/26.
 */

public class DataResponse {
    /**
     * 环信的文本消息发送功能，
     * message.setChatType(EMMessage.ChatType.ChatRoom);    如果有需求可以再加入此方法
     * @param content
     * @param toUserName
     */
    public void sendTextMessage(String content,String toUserName){
        EMMessage message = EMMessage.createTxtSendMessage(content,toUserName);
        Log.e("chatTo","inDataResponse  " + content + "to " + toUserName);
        sendMessage(message);
    }

    /**
     * 环信的语音发送功能
     * @param filePath
     * @param timeLength
     * @param toUserName
     */
    public void sendVoiceMessage(String filePath,int timeLength,String toUserName){
        EMMessage message = EMMessage.createVoiceSendMessage(filePath,timeLength,toUserName);
        sendMessage(message);
    }

    /**
     * 环信的视频发送功能
     * @param videofilePath
     * @param imageThumbPath    视频第一帧图缩略图
     * @param timeLength
     * @param toUserName
     */
    public void sendVideoMessage(String videofilePath,String imageThumbPath,int timeLength,String toUserName){
        EMMessage message = EMMessage.createVideoSendMessage(videofilePath,imageThumbPath,timeLength,toUserName);
        sendMessage(message);
    }

    /**
     * 环信的图片发送功能
     * @param filePath
     * @param sendOriginalImage 是否发送原图
     * @param toUserName
     */
    public void sendImageMessage(String filePath,boolean sendOriginalImage,String toUserName){
        Log.e("sendPHOTO","path =" + filePath + " to users Name" + toUserName);
        EMMessage message = EMMessage.createImageSendMessage(filePath,sendOriginalImage,toUserName);
        sendMessage(message);
    }

    /**
     * 位置消息
     * @param latitude 纬度
     * @param longitude 经度
     * @param locationAddress   位置详情
     * @param toUserName    接收人或群id
     */
    public void sendLocationMessage(double latitude,double longitude,String locationAddress,String toUserName){
        EMMessage message = EMMessage.createLocationSendMessage(latitude,longitude,locationAddress,toUserName);
        sendMessage(message);
    }

    /**
     * 发送文件消息
     * @param filePath
     * @param toUserName
     */
    public void sendFileMessage(String filePath,String toUserName){
        EMMessage message = EMMessage.createFileSendMessage(filePath,toUserName);
        sendMessage(message);
    }

    /**
     * 环信的发送方法（环信所有的信息都是通过此方法进行发送的）
     * 如果是群聊，则需要设置message的type
     * @param message
     */
    private void sendMessage(EMMessage message) {

        EMClient.getInstance().chatManager().sendMessage(message);
    }
    /*******************************************以下为消息的接收部分***********************************************/
    /**
     * 设置消息的接收方法监听回调
     * 记得在不需要的时候移除listener，如在activity的onDestroy()时
     * EMClient.getInstance().chatManager().removeMessageListener(msgListener);
     * @param emMessageListener
     * 该方法的回调方法内包含了各种需要响应的消息，包括
     * 收到消息
     * 收到透传消息
     * 收到已读回执
     * 收到已送达回执
     * 消息状态变动
     */
    public void addMessageGetListener(EMMessageListener emMessageListener){
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
    }

    /**
     * 与添加消息接收方法相对应
     * @param emMessageListener
     */
    public void remoteMessageGetListener(EMMessageListener emMessageListener){
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
    }

    /**
     * 监听消息状态
     * @param message
     * @param emCallBack
     */
    public void setMessageStatusCallback(EMMessage message,EMCallBack emCallBack){
        message.setMessageStatusCallback(emCallBack);
    }

    /**                    此方法有待考证
     * 获取与某用户的所有聊天记录
     * 建议初始化SDK的时候设置成每个会话默认load一条消息，节省加载会话的时间，
     * 方法为： options.setNumberOfMessagesLoaded(1);
     * @param toUserName
     * @return
     */
    public void getAllMessages(final String toUserName, final EMMessageListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserName);
                if (conversation == null)return;
                //获取此会话的所有消息
                List<EMMessage> messages = conversation.getAllMessages();
                //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                //        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
                listener.onMessageReceived(messages);
                conversation.clear();
            }
        }).start();

    }

    /**
     * 获取与某人会话接受到的最后一条消息
     * @param toUserName
     * @return
     */
    public EMMessage getLastMessageFromOthers(String toUserName){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserName);
        EMMessage latestMessage = conversation.getLatestMessageFromOthers();
        return latestMessage;
    }

    /**
     * 获取与某用户的聊天消息总数
     * @param toUserName
     * @return
     */
    public int getUnReadMsgCount(String toUserName){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserName);
        return conversation.getUnreadMsgCount();
    }
    public void markAllConversationAsRead(String toUserName){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toUserName);
        //指定会话消息未读数清零
        conversation.markAllMessagesAsRead();
//把一条消息置为已读
        conversation.markMessageAsRead(toUserName);
//所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
    }

    public Map<String ,EMConversation> getAllConversations(){
        return EMClient.getInstance().chatManager().getAllConversations();
    }

}
