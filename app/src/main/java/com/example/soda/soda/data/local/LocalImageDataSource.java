package com.example.soda.soda.data.local;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.soda.soda.data.bean.ImageFloder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by soda on 2017/7/31.
 */

public class LocalImageDataSource {
    public void getList(final ContentResolver contentResolver , final LocalDataSourceCallBack.GetLocalImagesCallBack callBack){
        final List<ImageFloder> mImageFloders = new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            callBack.onNoExternalStorage();
        }
        callBack.onStartGetImage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = contentResolver;

                //查询jpeg和png的图片
                Cursor cursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Log.e("TAG","mCursor count =" + cursor.getCount());

                HashSet<String> mDirPaths = new HashSet<String>();
                int mPicSize = 0;
                File mImageDir = null;
                int totalCount = 0;
                while (cursor.moveToNext())
                {
                    String path = cursor.
                            getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.e("TAG","第一张图片的路径" + path);
                    if (firstImage == null){
                        firstImage = path;
                    }
                    File parentFlie = new File(path).getParentFile();
                    if (parentFlie == null)
                        continue;
                    String dirPath = parentFlie.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    totalCount ++;

                    //利用HashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else {
                        mDirPaths.add(dirPath);
                        //初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    if (parentFlie.list() == null)continue;//防止出现空指针
                    int picSize = parentFlie.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicSize){
                        mPicSize = picSize;
                        mImageDir = parentFlie;//最多文件的文件夹路径
                        Log.e("TAG","最多文件的文件夹路径" + mImageDir);
                    }
                }

                cursor.close();
                //释放用于辅助的HashDir
                mDirPaths = null;
                callBack.onGetLocalImageData(mImageFloders,mImageDir,totalCount);
            }
        }).start();

    }
}
