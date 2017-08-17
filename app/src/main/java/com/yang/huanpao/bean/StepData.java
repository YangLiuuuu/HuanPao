package com.yang.huanpao.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by yang on 2017/6/23.
 */

public class StepData extends DataSupport{

    private int id;

    private String today;

    private String step;

    public StepData(int id, String today, String step) {
        this.id = id;
        this.today = today;
        this.step = step;
    }

    public StepData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", today='" + today + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
