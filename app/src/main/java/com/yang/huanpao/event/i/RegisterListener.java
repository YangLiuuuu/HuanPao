package com.yang.huanpao.event.i;

import com.yang.huanpao.bean.User;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by yang on 2017/8/11.
 */

public interface RegisterListener {
    void onSuccess(User user);
    void onFailure(BmobException e);
}
