package com.example.soda.soda.chatting;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.soda.soda.R;
import com.example.soda.soda.R2;
import com.example.soda.soda.data.DataSourceResponse;
import com.example.soda.soda.photo.PhotoActivity;
import com.example.soda.soda.util.ActivityUtils;
import com.hyphenate.chat.EMMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChattingActivity extends AppCompatActivity {
    public static void startActivity(Context context,String userId){
        Intent intent = new Intent(context,ChattingActivity.class);
        intent.putExtra("userId",userId);
        context.startActivity(intent);
    }

    //用于容纳用户输入文字的EditText
    @BindView(R2.id.chat_input_text_chatting_activity)
    EditText inputText;
    @BindView(R2.id.chat_send_button_chatting_activity)
    Button sendButton;
    @BindView(R2.id.text_chat_to_chatting_activity)
    TextView chatTo;

    ChattingFragment mFragment;
    ChattingPresenter mPresenter;
    DataSourceResponse mDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        checkAudioPermission();

        mFragment = (ChattingFragment) getSupportFragmentManager().findFragmentById(R.id.chatting_content_fragment);
        if (mFragment == null){
            mFragment = new ChattingFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mFragment,R.id.chatting_content_fragment);
        }
        if (mDataModel == null){
            mDataModel = DataSourceResponse.getInstance();
        }
        if (mPresenter == null){
            mPresenter = new ChattingPresenter(mDataModel,mFragment);
        }
        Intent intent = getIntent();
        String toUserId = intent.getStringExtra("userId");
        mFragment.setPresenter(mPresenter);
        mFragment.setmChattingToUserId(toUserId);

        chatTo.setText(mFragment.getmChattingToUserId());
    }
    //TODO 检查有没有录制权限
    private void checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECORD_AUDIO
            },3);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },4);
        }
    }

    @OnClick(R2.id.chat_send_button_chatting_activity)
    void sendTxtButton(View view){
        String inputContent = inputText.getText().toString();
        if (inputContent.length() == 0){
            Toast.makeText(this,"您好像忘了输入内容，我还没学会读心术哦！",Toast.LENGTH_SHORT).show();
        }else {
            inputText.setText("");
            Log.e("chatTo",mFragment.getmChattingToUserId());
            mPresenter.SendMessage(mFragment.getmChattingToUserId(),inputContent,EMMessage.Type.TXT);
        }
    }
    @OnClick(R2.id.chat_send_audio_button_chatting_activity)
    void sendAudioButton(View view){

    }

    public static final int REQUESTCODE_GETPHOTO = 1;
    public static final int REQUESTCODE_TAKINGPIC = 2;
    @OnClick(R2.id.menu_bottom_iamge)
    void selectPhoto(View view){
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivityForResult(intent, REQUESTCODE_GETPHOTO);
    }

    //TODO 图片发送功能还需要进行进一步封装
    @OnClick(R2.id.menu_bottom_taking_pic)
    void takingPicture(View view){
        if (ContextCompat.checkSelfPermission(ChattingActivity.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChattingActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },2);
        }else {
            uri = setAndGetUri(getApplicationContext());
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(intent,REQUESTCODE_TAKINGPIC);
        }
    }
    Uri uri;
    String name;
    private Uri setAndGetUri(Context context) {
        name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME,name +".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE_GETPHOTO){
            if (resultCode == RESULT_OK){

            }
        }else if (requestCode == REQUESTCODE_TAKINGPIC){
            if (resultCode == RESULT_OK){
                String filePath = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (DocumentsContract.isDocumentUri(getApplicationContext(),uri)){
                        //如果是document类型的 uri，则通过document id 来进行处理
                        String documentId = DocumentsContract.getDocumentId(uri);
                        if (isMediaDocument(uri)){
                            String id = documentId.split(":")[1];

                            String selection = MediaStore.Images.Media._ID + "=?";
                            String[] selectionArgs = {id};
                            filePath = getDataColumn(getApplicationContext(),MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    selection,selectionArgs);
                        }else if (isDownloadsDocument(uri)) { // DownloadsProvider
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                            filePath = getDataColumn(getApplicationContext(), contentUri, null, null);
                        }
                    } else if ("content".equalsIgnoreCase(uri.getScheme())){
                        // 如果是 content 类型的 Uri
                        filePath = getDataColumn(getApplicationContext(), uri, null, null);
                    } else if ("file".equals(uri.getScheme())) {
                        // 如果是 file 类型的 Uri,直接获取图片对应的路径
                        filePath = uri.getPath();
                    }

                    mDataModel.sendImageMessage(filePath,false,mFragment.getmChattingToUserId());

                }

            }
        }
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }




    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}
