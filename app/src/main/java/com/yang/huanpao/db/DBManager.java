package com.yang.huanpao.db;

/**
 * Created by yang on 2017/8/13.
 */

public class DBManager {
    private DBManager(){}
    private static DBManager myInstance = new DBManager();
    public static DBManager getInstance(){
        return myInstance;
    }

}
