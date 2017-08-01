package com.example.soda.soda.data.remote;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;

/**
 * Created by soda on 2017/7/26.
 */

public class StatusResponse {
    /**
     * 环信的登录方法
     * @param userName  用户账号
     * @param password  用户密码
     * @param loginCallback 回调接口
     */
    public void userLogin(String userName, String password, EMCallBack loginCallback){
        EMClient.getInstance().login(userName,password,loginCallback);
    }

    /**
     * 环信的登出同步方法
     * @param unBindToken   是否解绑第三方推送Token
     */
    public void userLogout(boolean unBindToken){
        EMClient.getInstance().logout(unBindToken);
    }

    /**
     * 环信的异步登出方法
     * @param unBindToken
     * @param logoutCallback
     */
    public void userLogout(boolean unBindToken,EMCallBack logoutCallback){
        EMClient.getInstance().logout(unBindToken,logoutCallback);
    }

    /**
     * 添加环信的连接状态监听
     * @param connectionListener
     */
    public void addConnctionStatusListener(EMConnectionListener connectionListener){
        EMClient.getInstance().addConnectionListener(connectionListener);
    }
}
