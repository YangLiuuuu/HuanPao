package com.yang.huanpao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by yang on 2017/6/23.
 */

@Entity
public class StepData {

    @Id
    private int id;

    private String today;

    private String step;

    @Generated(hash = 1510577093)
    public StepData(int id, String today, String step) {
        this.id = id;
        this.today = today;
        this.step = step;
    }

    @Generated(hash = 90761876)
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
