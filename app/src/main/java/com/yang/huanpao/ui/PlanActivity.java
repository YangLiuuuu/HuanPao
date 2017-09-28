package com.yang.huanpao.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.util.SharePreferencesUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yang on 2017/6/29.
 */

public class PlanActivity extends BaseActivity implements View.OnClickListener{

    private LinearLayout layout_titlebar;
    private ImageView iv_left;
    private ImageView iv_right;
    private EditText edit_step_number,edit_week_plan_number;
    private CheckBox cb_remind;
    private TextView tv_remind_time;
    private Button btn_save;
    private String walk_qty;
    private String remind;
    private String achieveTime;

    private void assignViews() {
        layout_titlebar = (LinearLayout) findViewById(R.id.layout_titlebar);
        iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        edit_step_number = (EditText) findViewById(R.id.edit_step_number);
        edit_week_plan_number = (EditText) findViewById(R.id.edit_week_plan_number);
        edit_step_number.setOnClickListener(this);
        edit_week_plan_number.setOnClickListener(this);
        cb_remind = (CheckBox) findViewById(R.id.cb_remind);
        tv_remind_time = (TextView) findViewById(R.id.tv_remind_time);
        btn_save = (Button) findViewById(R.id.btn_save);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        assignViews();
        initData();
        addListener();
    }

    private void addListener() {
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        tv_remind_time.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    private void initData() {
        String planWalk_QTY = SharePreferencesUtil.getString(this,"plan_walk");
        String remind = SharePreferencesUtil.getString(this,"remind");
        String achieveTime = SharePreferencesUtil.getString(this,"achieve_time");
        if (!planWalk_QTY.isEmpty()){
            if ("0".equals(planWalk_QTY)){
                edit_step_number.setText("7000");
            }else {
                edit_step_number.setText(planWalk_QTY);
            }
        }
        if (!remind.isEmpty()){
            if ("0".equals(remind)){
                cb_remind.setChecked(false);
            }else if ("1".equals(remind)){
                cb_remind.setChecked(true);
            }
        }
        if (!achieveTime.isEmpty()){
            tv_remind_time.setText(achieveTime);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_save:
                save();
                this.finish();
                Log.i("!!!!!!!", "save click");
                break;
            case R.id.tv_remind_time:
                showTimeDialog1();
                break;
            case R.id.edit_step_number:
                ((EditText)v).setSelection(((EditText) v).getText().length());
                break;
        }
    }

    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final DateFormat df = new SimpleDateFormat("HH:mm");

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                String remaintTime = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date!= null){
                    calendar.setTime(date);
                }
                tv_remind_time.setText(df.format(date));
            }
        },hour,minute,true).show();
    }

    private void save() {
        walk_qty = edit_step_number.getText().toString();
        if (cb_remind.isChecked()){
            remind = "1";
        }else {
            remind = "0";
        }
        achieveTime = tv_remind_time.getText().toString().trim();
        if (walk_qty.isEmpty()||"0".equals(walk_qty)){
//            sp.setParam("planWalk_QTY","7000");
            SharePreferencesUtil.put(this,"plan_walk","7000");
        }else {
//            sp.setParam("planWalk_QTY",walk_qty);
            SharePreferencesUtil.put(this,"plan_walk",walk_qty);
        }
//        sp.setParam("remind",remind);
        SharePreferencesUtil.put(this,"remind",remind);

        if (achieveTime.isEmpty()){
//            sp.setParam("achieveTime","21:00");
            SharePreferencesUtil.put(this,"achieve_time","8:00");
            this.achieveTime = "8:00";
        }else {
//            sp.setParam("achieveTime", achieveTime);
            SharePreferencesUtil.put(this,"achieve_time",achieveTime);

        }
    }
}
