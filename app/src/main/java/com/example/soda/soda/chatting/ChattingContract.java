package com.example.soda.soda.chatting;

import com.example.soda.soda.BasePresenter;
import com.example.soda.soda.BaseView;

/**
 * Created by soda on 2017/7/30.
 */

public interface ChattingContract {
    interface Presenter extends BasePresenter{}
    interface View extends BaseView<Presenter>{

        void OnGetTxtMessage(String fromUserId, String txtContent, long msgTime);

        void onSendMessage(String toUserId, String inputContent, long currentTimeMillis);

        void OnGetImageMessage(String fromUserId, String thumbnailUrl, long msgTime);

        String getmChattingToUserId();
    }
}
