package com.yang.huanpao.step.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by yang on 2017/6/24.
 */

public abstract class CountDownTimer {

    private final long mMillsInFuture;

    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    private boolean mCancelled = false;

    private static final int MSG = 1;

    public CountDownTimer(long mMillsInFuture, long mCountdownInterval) {
        this.mMillsInFuture = mMillsInFuture;
        this.mCountdownInterval = mCountdownInterval;
    }

    protected final void cancel(){
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }

    public  synchronized final CountDownTimer start(){
        if (mMillsInFuture <= 0){
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillsInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        return this;
    }

    public abstract void onFinish();

    public abstract void onTick(long millisUntilFinished);

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownTimer.this){
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();

                if (millisLeft <= 0){
                    onFinish();
                }else if (millisLeft < mCountdownInterval){
                    sendMessageDelayed(obtainMessage(MSG),millisLeft);
                }else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);

                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    while (delay < 0)delay += mCountdownInterval;

                    if (!mCancelled){
                        sendMessageDelayed(obtainMessage(MSG),delay);
                    }
                }
            }
        }
    };
}
