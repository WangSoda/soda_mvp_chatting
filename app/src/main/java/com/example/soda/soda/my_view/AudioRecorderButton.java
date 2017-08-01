package com.example.soda.soda.my_view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.soda.soda.R;
import com.example.soda.soda.data.audio.AudioMessageManager;


/**
 * Created by soda on 2017/8/1.
 */

public class AudioRecorderButton extends AppCompatButton implements AudioMessageManager.AudioStateListener {

    private static final int DISTANCE_Y = 200;
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurState = STATE_NORMAL;
    //已经开始录音
    private boolean isRecording;

    private DialogManager mDialogManager;

    private AudioMessageManager mAudioManger;

    private float mTime;
    //是否触发 LongClick
    private boolean mIsLongClick;

    public AudioRecorderButton(Context context) {
        this(context,null);
    }

    public AudioRecorderButton(final Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(getContext());

        String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/message_audios/";
        mAudioManger = AudioMessageManager.getInstance(dir);
        mAudioManger.setOnAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mIsLongClick = true;

                mAudioManger.prepareAudio();
                return false;
            }
        });
    }

    /**
     * 录音完成 后的回调
     */
    public interface AudioFinishRecorderListener{
        void onFinish(float seconds,String filePath);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
        mListener = listener;
    }

    //获取音量大小的
    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRecording){
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0x100;
    private static final int MSG_VOICE_CHANGED = 0x101;
    private static final int MSG_DIALOG_DIMISS = 0x102;
    private  int maxLevel = 7;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    //当准备完毕后 应该开启新的线程获取音量的变化
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManger.getVoiceLevel(maxLevel));
                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;
            }
        }
    };
    @Override
    public void wellPrepared() {
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int y = (int) event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:

                changeState(STATE_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isRecording){
                    if (wantToCancel(y)){
                        changeState(STATE_WANT_TO_CANCEL);
                    }else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (!mIsLongClick){
                    reset();
                    return super.onTouchEvent(event);
                }

                if (!isRecording | mTime < 0.6f){
                    mDialogManager.tooShort();
                    if (mAudioManger != null){
                        mAudioManger.cancel();
                        mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);
                    }
                }else if (mCurState == STATE_WANT_TO_CANCEL){

                    mDialogManager.dimissDialog();
                    mAudioManger.cancel();
                }else if (mCurState == STATE_RECORDING){//正常录制结束

                    mDialogManager.dimissDialog();
                    mAudioManger.release();
                    if (mListener != null){
                        mListener.onFinish(mTime,mAudioManger.getCurrentFilePath());
                    }

                }
                mDialogManager.dimissDialog();
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态以及标志位
     */
    private void reset(){
        isRecording = false;
        mIsLongClick = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancel( int y) {
        if (y < -DISTANCE_Y){
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState != state){
            mCurState = state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_recording);
                    if (isRecording){

                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }


}
