package com.yang.huanpao.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.yang.huanpao.R;
import com.yang.huanpao.event.OnTabSelectedListener;
import com.yang.huanpao.ui.fragment.StepCountFragment;
import com.yang.huanpao.ui.fragment.TempFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnTabSelectedListener{

    @BindView(R.id.step_count_tab)
    public RelativeLayout step_count_tab;

    @BindView(R.id.magazine_tab)
    public RelativeLayout magazine_tab;

    @BindView(R.id.dynamic_tab)
    public RelativeLayout dynamic_tab;

    @BindView(R.id.mine_tab)
    public RelativeLayout mine_tab;

    private RelativeLayout[] mTabs;

    private int currentTabSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(((Toolbar)findViewById(R.id.toolbar)));
        initTab();
    }

    private void initTab() {
        step_count_tab.setOnClickListener(this);
        magazine_tab.setOnClickListener(this);
        dynamic_tab.setOnClickListener(this);
        mine_tab.setOnClickListener(this);
        mTabs = new RelativeLayout[4];
        mTabs[0] = step_count_tab;
        mTabs[1] = magazine_tab;
        mTabs[2] = dynamic_tab;
        mTabs[3] = mine_tab;
        StepCountFragment stepCountFragment = new StepCountFragment();
        TempFragment t1 = new TempFragment();
        TempFragment t2 = new TempFragment();
        TempFragment t3 = new TempFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, stepCountFragment).
                add(R.id.fragment_container, t1)
                .add(R.id.fragment_container, t2)
                .add(R.id.fragment_container,t3)
                .hide(stepCountFragment).hide(t1)
                .hide(t2).hide(t3).show(stepCountFragment).commit();
    }

    @Override
    public void onClick(View view) {
        Log.i("liuyang","点击了" + view.getId());
        switch (view.getId()){
            case R.id.step_count_tab:
                currentTabSelected = 0;
                onTabSelected(currentTabSelected);
                break;
            case R.id.magazine_tab:
                currentTabSelected = 1;
                onTabSelected(currentTabSelected);
                break;
            case R.id.dynamic_tab:
                currentTabSelected = 2;
                onTabSelected(currentTabSelected);
                break;
            case R.id.mine_tab:
                currentTabSelected = 3;
                onTabSelected(currentTabSelected);
                break;
        }
    }

    @Override
    public void onTabSelected(int index) {
        for (int i = 0; i < 4; i++){
            if (i != index){
                mTabs[i].setBackgroundColor(Color.WHITE);
            }else {
                mTabs[i].setBackgroundColor(Color.GREEN);
            }
        }
        //此处必须让activity继承AppCompatActivity才能获得transaction
       FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index == 0){
            transaction.replace(R.id.fragment_container, new StepCountFragment());
        }else if (index == 1){
            transaction.replace(R.id.fragment_container,new TempFragment());
        }else if (index == 2){
            transaction.replace(R.id.fragment_container,new TempFragment());
        }else if (index == 3){
            transaction.replace(R.id.fragment_container,new TempFragment());
        }
        transaction.commit();
    }
}
