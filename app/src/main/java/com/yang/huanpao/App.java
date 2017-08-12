package com.yang.huanpao;

import android.app.Application;

import com.yang.huanpao.config.Const;

import cn.bmob.v3.Bmob;

/**
 * Created by yang on 2017/8/10.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Bmob.initialize(this, Const.APP_ID);
    }
}
