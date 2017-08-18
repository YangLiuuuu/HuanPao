package com.yang.huanpao;

import com.yang.huanpao.config.Const;
import com.yang.huanpao.util.SharePreferencesUtil;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Created by yang on 2017/8/10.
 */

public class App extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        setDatabase();
        Bmob.initialize(this, Const.BMOB_APP_ID);
        SharePreferencesUtil.put(this,"plan_walk","7000");
        SharePreferencesUtil.put(this,"remind","1");
        SharePreferencesUtil.put(this,"achieve_time","8:00");
    }

    private void setDatabase() {
        LitePal.getDatabase();
    }
}
