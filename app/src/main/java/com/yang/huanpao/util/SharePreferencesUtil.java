package com.yang.huanpao.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yang on 2017/8/12.
 */

public class SharePreferencesUtil {

    public static void put(Context context,String key, Object obj){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (obj instanceof String){
            edit.putString(key,(String)obj);
        }else if (obj instanceof Boolean){
            edit.putBoolean(key,(Boolean) obj);
        }else if (obj instanceof Integer){
            edit.putInt(key,(int)obj);
        }else if (obj instanceof Float){
            edit.putFloat(key, (Float) obj);
        }
        edit.apply();
    }

    public static String getString(Context context,String key){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString(key,null);
    }
    public static boolean getBoolean(Context context,String key){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getBoolean(key,false);
    }
    public static int getInt(Context context,String key){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt(key,-1);
    }
    public static float getFloat(Context context,String key){
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getFloat(key,-1f);
    }
}
