package com.yang.huanpao.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseActivity;
import com.yang.huanpao.step.service.StepService;
import com.yang.huanpao.step.util.UpdateCallBack;
import com.yang.huanpao.ui.fragment.StepCountFragment;
import com.yang.huanpao.ui.fragment.TempFragment;
import com.yang.huanpao.util.SharePreferencesUtil;

/**
 * Created by yang on 2017/8/14.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity2 extends BaseActivity{

//    @BindView(R.id.tab)
    public TabLayout tab;

    private Toolbar toolbar;

    public FrameLayout fragmentContainer;

    private ScrollView scrollview;

    final StepCountFragment stepCountFragment = new StepCountFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        ButterKnife.bind(this);
        tab = (TabLayout) findViewById(R.id.tab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.logo);
        toolbar.setTitle("环跑");

        setSupportActionBar(toolbar);

        setUpService();
        initTab();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.plan:
                startActivity(PlanActivity.class,null,false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setUpService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent,connection, Context.BIND_AUTO_CREATE);
        startService(intent);
        Log.i("!!!!!!!","startService");
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) SharePreferencesUtil.getString(MainActivity2.this,"plan_walk");
            stepCountFragment.getStepArcView().setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallBack(new UpdateCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = SharePreferencesUtil.getString(MainActivity2.this,"plan_walk");
                    stepCountFragment.getStepArcView().setCurrentCount(Integer.parseInt(planWalk_QTY),stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            setUpService();
        }
    };

    private void initTab() {
        tab.addTab(tab.newTab().setIcon(R.mipmap.ic_launcher).setText("计步"));
        tab.addTab(tab.newTab().setIcon(R.mipmap.ic_launcher).setText("杂志"));
        tab.addTab(tab.newTab().setIcon(R.mipmap.ic_launcher).setText("我的"));

        final TempFragment t1 = new TempFragment();
        final TempFragment t2 = new TempFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, stepCountFragment)
                .add(R.id.fragment_container, t1)
                .add(R.id.fragment_container, t2)
                .hide(stepCountFragment).hide(t1)
                .hide(t2)
                .show(stepCountFragment).commit();
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .show(stepCountFragment).hide(t1).hide(t2).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .hide(stepCountFragment).hide(t2).show(t1).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .hide(stepCountFragment).hide(t1).show(t2).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
