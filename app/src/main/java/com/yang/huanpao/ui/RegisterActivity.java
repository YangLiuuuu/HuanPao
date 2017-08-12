package com.yang.huanpao.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.bean.User;
import com.yang.huanpao.event.UserModel;
import com.yang.huanpao.event.i.RegisterListener;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by yang on 2017/8/11.
 */

public class RegisterActivity extends BaseActivity {

    private EditText account, password, checkpassword;
    private Button OkBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        account = (EditText) findViewById(R.id.register_account_edit);
        password = (EditText) findViewById(R.id.register_password_edit);
        checkpassword = (EditText) findViewById(R.id.register_check_password_edit);
        OkBtn = (Button) findViewById(R.id.register_ok_btn);
        OkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel.getInstance().register(RegisterActivity.this, account.getText().toString(), password.getText().toString(),
                        checkpassword.getText().toString(), new RegisterListener() {
                            @Override
                            public void onSuccess(User user) {
                                toast("注册成功，去登录吧");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data",user);
                                startActivity(LoginActivity.class,null,true);
                            }

                            @Override
                            public void onFailure(BmobException e) {
                                if (!isNetWorkConnected()){
                                    toast("无法连接网络");
                                }else {
                                    toast("注册失败，请重试  "+ e.getMessage());
                                }
                            }
                        });
            }
        });
    }
}
