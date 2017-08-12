package com.yang.huanpao.event;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yang.huanpao.bean.User;
import com.yang.huanpao.event.i.LoginListener;
import com.yang.huanpao.event.i.RegisterListener;

import java.util.Objects;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by yang on 2017/8/10.
 */

public class UserModel {
    private static UserModel ourInstance = new UserModel();

    public static UserModel getInstance() {
        return ourInstance;
    }
    private UserModel(){}

    public void login(Context context, String username, String password, final LoginListener listener){
        if(TextUtils.isEmpty(username)){
            Toast.makeText(context,"请填写用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请填写密码",Toast.LENGTH_SHORT).show();
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    listener.onSuccess(user);
                }else {
                    listener.onFailure(e);
                }
            }
        });
    }

    public void register(Context context, String username, String password, String checkPassword, final RegisterListener listener){
        if(TextUtils.isEmpty(username)){
            Toast.makeText(context,"请填写用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context,"请填写密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Objects.equals(password, checkPassword)){
            Toast.makeText(context,"两次密码不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        final User user =new User();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    listener.onSuccess(user);
                }else {
                    listener.onFailure(e);
                }
            }
        });
    }
}
