package com.example.soda.soda.data.local;

import com.example.soda.soda.data.bean.ImageFloder;

import java.io.File;
import java.util.List;

/**
 * Created by soda on 2017/7/31.
 */

public interface LocalDataSourceCallBack {
    interface GetLocalImagesCallBack{
        void onNoExternalStorage();

        /**
         *  @param mImageFloders 获取到的文件夹列表
         * @param mImageDir     最多文件的文件夹路径
         * @param totalCount
         */
        void onGetLocalImageData(List<ImageFloder> mImageFloders, File mImageDir, int totalCount);

        void onStartGetImage();
    }
}
