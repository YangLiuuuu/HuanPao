package com.yang.huanpao.step.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yang.huanpao.R;
import com.yang.huanpao.bean.StepData;
import com.yang.huanpao.step.accelerometer.StepCount;
import com.yang.huanpao.step.accelerometer.StepPassValueListener;
import com.yang.huanpao.step.util.CountDownTimer;
import com.yang.huanpao.step.util.DbUtil;
import com.yang.huanpao.step.util.UpdateCallBack;
import com.yang.huanpao.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yang on 2017/6/23.
 */

public class StepService extends Service implements SensorEventListener {

    private String TAG = "StepService";

    public static final int notifyId_Step = 100;

    public static final int notify_remind_id = 200;

    private int CURRENT_STEP;

    private static String CURRENT_DATA = "";

    private NotificationManager notificationManager;

    private Notification.Builder mBuilder;

    private BroadcastReceiver mReceiver;

    private static int duration = 30 * 1000;

    private SensorManager mSensorManager;

    private int stepSensorType;

    private StepCount mStepCount;

    private TimeCount time;

    private StepBinder mStepBinder = new StepBinder();

    private boolean hasRecord = false;

    private int hasStepCount = 0;

    private int previousStepCount = 0;

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopForeground(true);
        unregisterReceiver(mReceiver);
        Log.d(TAG, "StepService 关闭");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStepBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public int getStepCount() {
        return CURRENT_STEP;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initNotification();
        initTodayData();
        initBroadcastReceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
                Log.i("liuyang","开始计步");
            }
        }).start();
        startTimeCount();
    }

    private void initNotification() {
        mBuilder = new Notification.Builder(this);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + "步")
                .setContentIntent(getDefaultIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.logo);
        Notification notification = mBuilder.build();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(notifyId_Step, notification);
    }

    private void initTodayData() {
        CURRENT_DATA = getTodayData();
        DbUtil.createDb(this, "YangStepCount");
        DbUtil.getLiteOrm().setDebugged(false);
        List<StepData> list = DbUtil.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATA});
        if (list.size() == 0 || list.isEmpty()) {
            CURRENT_STEP = 0;
        } else if (list.size() == 1) {
            Log.d(TAG, "StepData=" + list.get(0).toString());
            CURRENT_STEP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.d(TAG, "出错了");
        }
    }

    private String getTodayData() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private void initBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        //灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        //亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.d(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d(TAG, "screen off");
                    //改为60秒一次存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.d(TAG, "screen unlock");
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                    Log.i(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(action)) {
                    Log.i(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
                    //日期改变，重置步数
                    save();
                    isNewDay();
                } else if (Intent.ACTION_TIME_CHANGED.equals(action)) {
                    isCall();
                } else if (Intent.ACTION_TIME_TICK.equals(action)) {//日期变化步数重置为0
                    isCall();
//                    Logger.d("重置步数" + StepDcretor.CURRENT_STEP);
                    save();
                    isNewDay();
                }
            }
        };
        registerReceiver(mReceiver, filter);
    }

    private PendingIntent getDefaultIntent(int flag) {
        return PendingIntent.getActivity(this, 1, new Intent(), flag);
    }

    private void isNewDay() {
        String time = "00:00";
        if (time.equals(new SimpleDateFormat("HH:mm").format(new Date())) || !CURRENT_DATA.equals(getTodayData())) {
            initTodayData();
        }
    }

    private void isCall() {
        String time = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("achieveTime", "21:00");
        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "7000");
        String remind = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("remind", "1");
        if ("1".equals(remind) && (CURRENT_STEP < Integer.parseInt(plan)) && (time.equals(new SimpleDateFormat().format(new Date())))) {
            remindNotify();
        }
    }

    /**
     * 提醒锻炼通知栏
     */
    private void remindNotify() {
        //设置点击跳转
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        String plan = this.getSharedPreferences("share_date", Context.MODE_MULTI_PROCESS).getString("planWalk_QTY", "7000");
        NotificationCompat.Builder mBuiler = new NotificationCompat.Builder(this);
        mBuiler.setContentTitle("今日步数" + CURRENT_STEP + "步")
                .setContentText("距离目标还差" + (Integer.parseInt(plan) - CURRENT_STEP) + "步，加油!")
                .setContentIntent(hangPendingIntent)
                .setTicker(getResources().getString(R.string.app_name) + "提醒")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.logo);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(notify_remind_id, mBuiler.build());
    }

    private void startStepDetector() {
        if (mSensorManager != null) {
            mSensorManager = null;
        }
        //获取传感器实例
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        int VERSION_CODES = Build.VERSION.SDK_INT;
        if (VERSION_CODES >= 19) {
            addCountStepListener();
        } else {
            addBasePedometerListener();
        }
    }

    /**
     * 添加传感器监听
     * 1. TYPE_STEP_COUNTER API的解释说返回从开机被激活后统计的步数，当重启手机后该数据归零，
     * 该传感器是一个硬件传感器所以它是低功耗的。
     * 为了能持续的计步，请不要反注册事件，就算手机处于休眠状态它依然会计步。
     * 当激活的时候依然会上报步数。该sensor适合在长时间的计步需求。
     * <p>
     * 2.TYPE_STEP_DETECTOR翻译过来就是走路检测，
     * API文档也确实是这样说的，该sensor只用来监监测走步，每次返回数字1.0。
     * 如果需要长事件的计步请使用TYPE_STEP_COUNTER。
     */
    private void addCountStepListener() {
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            Log.v(TAG, "Sensor.TYPE_STEP_COUNTER");
            mSensorManager.registerListener(StepService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            Log.v(TAG, "Sensor.TYPE_STEP_DETECTOR");
            mSensorManager.registerListener(StepService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.v(TAG, "Count sensor not available!");
            addBasePedometerListener();
        }
    }

    /**
     * 通过加速度传感器来记步
     */
    private void addBasePedometerListener() {
        mStepCount = new StepCount();
        // 获得传感器的类型，这里获得的类型是加速度传感器
        // 此方法用来注册，只有注册过才会生效，参数：SensorEventListener的实例，Sensor的实例，更新速率
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = mSensorManager.registerListener(mStepCount.getStepDetector(), sensor, SensorManager.SENSOR_DELAY_UI);
        mStepCount.initListener(new StepPassValueListener() {
            @Override
            public void stepChanged(int steps) {
                CURRENT_STEP = steps;
                updateNotification();
            }
        });
    }

    /**
     * 更新步数通知
     */
    private void updateNotification() {
        Intent hangIntent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = mBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("今日步数" + CURRENT_STEP + "步")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(hangPendingIntent)
                .setSmallIcon(R.mipmap.logo)
                .build();
        notificationManager.notify(notifyId_Step, notification);
        if (mCallBack != null) {
            mCallBack.updateUi(CURRENT_STEP);
        }
        Log.d(TAG, "updateNotification()");
    }

    private UpdateCallBack mCallBack;

    public void registerCallBack(UpdateCallBack callBack) {
        this.mCallBack = callBack;
    }

    private void save() {
        int tempStep = CURRENT_STEP;
        List<StepData> list = DbUtil.getQueryByWhere(StepData.class, "today", new String[]{CURRENT_DATA});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENT_DATA);
            data.setStep(tempStep + "");
            DbUtil.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtil.update(data);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "onSensorChanged: ");
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //获取当前传感器返回的临时数据
            int tempStep = (int) event.values[0];
            //首次如果没有获取手机系统中已有步数则获取一次系统中app还未开始记录的步数
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                //获取APP打开到现在的总步数=本次系统回调的总步数-APP打开之前已有的步数
                int thisStepCount = tempStep - hasStepCount;
                //本次有效步数=（APP打开后所记录的总步数-上一次APP打开后所记录的总步数）
                int thisStep = thisStepCount - previousStepCount;
                //总步数=现有的步数+本次有效步数
                CURRENT_STEP += (thisStep);
                //记录最后一次APP打开到现在的总步数
                previousStepCount = thisStepCount;
            }
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                CURRENT_STEP++;
            }
        }
        updateNotification();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class StepBinder extends Binder {

        public StepService getService() {
            return StepService.this;
        }
    }

    private class TimeCount extends CountDownTimer {

        public TimeCount(long mMillsInFuture, long mCountdownInterval) {
            super(mMillsInFuture, mCountdownInterval);
        }

        @Override
        public void onFinish() {
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    private void startTimeCount() {
        if (time == null) {
            time = new TimeCount(duration, 1000);
        }
        time.start();
    }
}
