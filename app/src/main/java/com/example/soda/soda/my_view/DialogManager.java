package com.example.soda.soda.my_view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soda.soda.R;

/**
 * Created by soda on 2017/8/1.
 */

public class DialogManager {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    private Context mContext;

    public DialogManager(Context context){
        mContext = context;
    }
    public void showRecordingDialog(){
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_recorder,null);
        mDialog.setContentView(view);

        mIcon = mDialog.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = mDialog.findViewById(R.id.id_recorder_dialog_voice);

        mLable = mDialog.findViewById(R.id.id_recorder_dialog_label);

        mDialog.show();
    }

    public void recording(){
        if (mDialog != null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("手指上划， 取消发送");
        }
    }

    public void wantToCancel(){
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指，取消发送");
        }
    }

    public void tooShort(){
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.voice_to_short);
            mLable.setText("录音时间过段");
        }
    }

    public void dimissDialog(){
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public void updateVoiceLevel(int level){
        if (mDialog != null && mDialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            int resId = mContext.getResources().getIdentifier(
                    "v" + level,"drawable",mContext.getPackageName());//避免代码冗余

            mVoice.setImageResource(resId);
        }
    }

}
