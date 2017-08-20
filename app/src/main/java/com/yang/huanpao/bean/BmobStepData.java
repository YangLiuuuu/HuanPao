package com.yang.huanpao.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by yang on 2017/6/23.
 */

public class BmobStepData extends BmobObject{

    private String today;

    private String step;


    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BmobStepData(int id, String today, String step) {
        this.today = today;
        this.step = step;
    }

    public BmobStepData() {
    }


    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "StepData{" +
                ", today='" + today + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
