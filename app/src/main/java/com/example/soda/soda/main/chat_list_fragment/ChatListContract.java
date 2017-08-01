package com.example.soda.soda.main.chat_list_fragment;

import android.view.View;

import com.example.soda.soda.BasePresenter;
import com.example.soda.soda.BaseView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by soda on 2017/7/29.
 */

public interface ChatListContract  {
    interface Presenter extends BasePresenter{
        void onUserClickItem(String toUserId);
    }
    interface View extends BaseView<Presenter>{

        void startChattingActivity(String toUserId);

        void OnGetTxtMessage(String fromUserId, String txtContent, long msgTime);

        void OnGetMediaMessage(String fromUserId, long msgTime, EMMessage.Type type);

        void upDateChatList(Map<String, EMConversation> conversationMap);
    }
}
