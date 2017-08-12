package com.yang.huanpao.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.util.SharePreferencesUtil;

/**
 * Created by yang on 2017/8/12.
 */

public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    startActivity(MainActivity.class,null,true);
                    break;
                case 0:
                    startActivity(LoginActivity.class,null,true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (SharePreferencesUtil.getBoolean(this,"isLogin")){
            Message msg = Message.obtain();
            msg.what = 1;
            mHandler.sendMessageDelayed(msg,500);
        }else {
            Message msg = Message.obtain();
            msg.what = 0;
            mHandler.sendMessageDelayed(msg,500);
        }
    }
}
