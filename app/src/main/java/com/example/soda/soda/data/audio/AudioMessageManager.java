package com.example.soda.soda.data.audio;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by soda on 2017/8/1.
 */

public class AudioMessageManager {
    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioMessageManager mInstance;

    private AudioMessageManager(String dir){
        mDir = dir;
    }

    public static AudioMessageManager getInstance(String dir){
        if (mInstance == null){
            synchronized (AudioMessageManager.class){
                if (mInstance == null){
                    mInstance = new AudioMessageManager(dir);
                }
            }
        }
        return mInstance;
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    /**
     * 准备完毕的回调接口
     */
    public interface AudioStateListener{
        void wellPrepared();
    }

    public AudioStateListener mListener;

    private boolean isPrepared;



    public void setOnAudioStateListener(AudioStateListener listener){
        mListener = listener;
    }


    public void prepareAudio(){

                try {
                    isPrepared = false;
                    File dir = new File(mDir);
                    if (!dir.exists()){
                        dir.mkdirs();
                    }
                    String fileName = generateFileName();
                    File file = new File(dir, fileName);
                    if (!file.exists()){
                        file.createNewFile();
                    }

                    mCurrentFilePath = file.getAbsolutePath();

                    mMediaRecorder = new MediaRecorder();
                    //设置输出文件
                    mMediaRecorder.setOutputFile(mCurrentFilePath);
                    //设置MediaRecorder的音频源为麦克风
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                    //这里可以进行版本判断，如果小于10 RAW_AMR
                    //设置音频格式
                    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                    //设置音频编码
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    //准备结束
                    Log.e("testResult","准备结束");
                    isPrepared = true;

                    if (mListener != null ){
                        mListener.wellPrepared();
                    }else {
                        Log.e("TAG","wellPrepared listener == null");
                    }
                } catch (IllegalStateException| IOException e) {
                    e.printStackTrace();
                }


    }

    /**
     * 随机生成文件的名称
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if (isPrepared){
            //mMediaRecorder.getMaxAmplitude() 振幅在 1 - 32767
            try {

                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            }catch (Exception e){}
        }
        return 1;
    }

    /**
     * 保存文件，正常结束
     */
    public void release() {
        if (mMediaRecorder == null)return;
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

    }

    /**
     * 取消保存，会删除录音文件
     */
    public void cancel(){
        release();
        if (mCurrentFilePath != null){
            File file = new File(mCurrentFilePath);
            if (file.exists())
            file.delete();
        }
        mCurrentFilePath = null;
    }
}
