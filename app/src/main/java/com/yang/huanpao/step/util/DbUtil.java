package com.yang.huanpao.step.util;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.List;

/**
 * Created by yang on 2017/6/23.
 */

public class DbUtil {

    public static String DB_NAME;
    public static LiteOrm liteOrm;

    public static void createDb(Context context, String DB_NAME){
        DB_NAME = DB_NAME + ".db";
        if (liteOrm == null){
            liteOrm = LiteOrm.newCascadeInstance(context,DB_NAME);
            liteOrm.setDebugged(true);
        }
    }

    public static LiteOrm getLiteOrm(){
        return liteOrm;
    }

    /**
     * 插入一条记录
     * @param t
     * @param <T>
     */
    public static <T>void insert(T t){
        liteOrm.save(t);
    }

    /**
     * 插入所有记录
     * @param list
     * @param <T>
     */
    public static <T>void insertAll(List<T> list){
        liteOrm.save(list);
    }

    /**
     * 查询所有
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> List<T> getQueryAll(Class<T> cla){
        return liteOrm.query(cla);
    }

    /**
     *查询某字段等于value的值
     * @param cla
     * @param field
     * @param value
     * @param <T>
     * @return
     */
    public static <T> List<T> getQueryByWhere(Class<T> cla , String field, String[] value){
        return liteOrm.<T>query(new QueryBuilder<T>(cla).where(field + "=?",value));
    }

    /**
     * 查询 某字段等于value的值 可以指定从1-20，就是分页
     * @param cla
     * @param field
     * @param value
     * @param start
     * @param length
     * @param <T>
     * @return
     */
    public static <T> List<T> getQueryByWhereLength(Class<T> cla, String field, String[]value, int start, int length){
        return liteOrm.<T>query(new QueryBuilder<T>(cla).where(field + "=?",value).limit(start,length));
    }

    /**
     * 删除所有某字段等于value的值
     * @param cla
     * @param field
     * @param value
     * @param <T>
     * @return
     */
    public static<T> int deleteWhere(Class<T> cla, String field, String[] value){
        return liteOrm.delete(cla,new WhereBuilder(cla).where(field + "=?",value));
    }

    /**
     * 删除所有
     * @param cla
     * @param <T>
     */
    public static<T>void deleteAll(Class<T> cla){
        liteOrm.delete(cla);
    }

    /**
     * 仅在已存在时更新
     * @param t
     * @param <T>
     */
    public static <T>void update(T t){
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    public static <T>void updateAll(List<T> list){
        liteOrm.update(list);
    }

    public static void closeDb(){
        liteOrm.close();
    }

}
