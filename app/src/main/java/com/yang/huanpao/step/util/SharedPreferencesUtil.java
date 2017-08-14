package com.yang.huanpao.step.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yang on 2017/6/23.
 */

public class SharedPreferencesUtil {
    private Context context;

    /**
     * 保存在手机里的文件名
     */
    private String FILE_NAME = "share_date";

    public SharedPreferencesUtil(String FILE_NAME){
        this.FILE_NAME = FILE_NAME;
    }
    public SharedPreferencesUtil(Context context){
        this.context = context;
    }

    public void setParam(String key, Object object){
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)){
            editor.putString(key,object.toString());
        }else if ("Integer".equals(type)){
            editor.putInt(key, (Integer) object);
        }else if ("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean) object);
        }else if ("Float".equals(type)){
            editor.putFloat(key, (Float) object);
        }else if ("Long".equals(type)){
            editor.putLong(key, (Long) object);
        }
        editor.apply();
    }


    /**
     * 得到保存数据的方法，通过默认值得到保存数据的类型，然后调用相关方法获得
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 删除数据的方法
     */
    public void remove(String key){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 清空数据
     */
    public void clear(){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }


}
