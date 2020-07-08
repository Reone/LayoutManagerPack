package com.reone.layoutmanagerdemo;

import android.app.Application;

import com.reone.layoutmanagerdemo.utils.ToastUtils;

/**
 * Created by wangxingsheng on 2020/7/8.
 * desc:
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}