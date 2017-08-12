package com.yang.huanpao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yang.huanpao.config.Const;

/**
 * Created by yang on 2017/8/11.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public  void startActivity(Class<? extends Activity>target, Bundle bundle, boolean isFinishCurrent){
        Intent  intent = new Intent();
        intent.setClass(this,target);
        if (bundle != null){
            intent.putExtra(getPackageName(),bundle);
        }
        startActivity(intent);
        if (isFinishCurrent){
            finish();
        }
    }

    public Bundle getBundle(){
        if (getIntent() != null && getIntent().hasExtra(getPackageName())){
            return getIntent().getBundleExtra(getPackageName());
        }else {
            return null;
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView(){
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN){
            if (getCurrentFocus() != null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**Log日志
     * @param msg
     */
    public void log(String msg){
        if(Const.isDebug){
            Log.i(this.getClass().getName(),msg);
        }
    }

    private Toast toast;
    public void toast(final Object obj){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toast == null){
                        toast = Toast.makeText(BaseActivity.this,"",Toast.LENGTH_SHORT);
                    }
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络连接
     * @return
     */
    public boolean isNetWorkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null){
            return networkInfo.isAvailable();
        }
        return false;
    }

    public int getScreenWidth(){
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
