package com.yibao.biggirl.model.app;

import com.yibao.biggirl.model.android.AndroidAndGirl;

import java.util.List;

/**
 * Author：Sid
 * Des：${TODO}
 * Time:2017/4/25 22:59
 */
public interface AppDataSource {
    //这个接口用于将数据回调给对应Presenter层
    interface LoadADataCallback {
        void onLoadData(List<AndroidAndGirl> list);

        void onDataNotAvailable();

    }

    void getApp(int page, int size, String type, LoadADataCallback callback);

}