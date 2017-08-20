package com.yang.huanpao.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.bean.User;
import com.yang.huanpao.config.Const;
import com.yang.huanpao.event.UserModel;
import com.yang.huanpao.event.i.LoginListener;
import com.yang.huanpao.util.SharePreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by yang on 2017/8/10.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText edit_account,edit_password;
    private Button btn_login;
    private TextView tex_register,login_by_qq;
    private Tencent mTencent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_account = (EditText) findViewById(R.id.edit_account);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_account.setText("123456");
        edit_password.setText("123456");
        btn_login = (Button) findViewById(R.id.btn_login);
        tex_register = (TextView) findViewById(R.id.tex_register);
        tex_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        login_by_qq = (TextView) findViewById(R.id.login_by_qq);
        login_by_qq.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String username = edit_account.getText().toString();
                String password = edit_password.getText().toString();
                UserModel.getInstance().login(this, username, password, new LoginListener() {
                    @Override
                    public void onSuccess(User user) {
                        toast("登录成功");
                        String userId = BmobUser.getCurrentUser(User.class).getObjectId();
                        SharePreferencesUtil.put(LoginActivity.this,"newUserId",userId);
                        SharePreferencesUtil.put(LoginActivity.this,"isLogin",true);
                        startActivity(MainActivity.class,null,true);
                    }

                    @Override
                    public void onFailure(BmobException e) {
                        if (!isNetWorkConnected()){
                            toast("无法连接网络");
                        }else {
                            toast("登录失败，请重试  " + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
                break;
            case R.id.tex_register:
                edit_account.setText("");
                edit_account.setText("");
                startActivity(RegisterActivity.class,null,false);
                break;
            case R.id.login_by_qq:
                mTencent = Tencent.createInstance(Const.Tencent_APP_ID,this.getApplicationContext());
                if (!mTencent.isSessionValid()){
                    mTencent.login(this, "all", new IUiListener() {
                        @Override
                        public void onComplete(Object arg0) {
                            // TODO Auto-generated method stub
                            if(arg0!=null){
                                JSONObject jsonObject = (JSONObject) arg0;
                                try {
                                    String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                                    String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                                    String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                                    BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                                    loginWithAuth(authInfo);
                                } catch (JSONException e) {
                                }
                            }
                        }

                        @Override
                        public void onError(UiError arg0) {
                            // TODO Auto-generated method stub
                            toast("QQ授权出错："+arg0.errorCode+"--"+arg0.errorDetail);
                        }

                        @Override
                        public void onCancel() {
                            // TODO Auto-generated method stub
                            toast("取消qq授权");
                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, new IUiListener() {
            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if(arg0!=null){
                    JSONObject jsonObject = (JSONObject) arg0;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                        loginWithAuth(authInfo);
                    } catch (JSONException e) {
                    }
                }
            }

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                toast("QQ授权出错："+arg0.errorCode+"--"+arg0.errorDetail);
            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                toast("取消qq授权");
            }
        });
    }

    private void loginWithAuth(BmobUser.BmobThirdUserAuth authInfo) {
        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
            @Override
            public void done(JSONObject jsonObject, BmobException e) {
                if (e == null){
                    toast("登录成功");
                    SharePreferencesUtil.put(LoginActivity.this,"isLogin",true);
                    startActivity(MainActivity.class,null,true);
                }else {
                    toast("登录失败 : " + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}
