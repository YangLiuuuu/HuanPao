package com.yang.huanpao.step.accelerometer;

/**
 * Created by yang on 2017/6/23.
 */

public class StepCount implements StepCountListener{

    private int count = 0;
    private int mCount = 0;
    private StepPassValueListener mStepPassValueListener;
    private long timeOfLastPeak = 0;
    private long timeOfThisPeak = 0;
    private StepDetector stepDetector;

    public StepCount() {
        this.stepDetector = new StepDetector();
        stepDetector.initListener(this);
    }

    public StepDetector getStepDetector(){
        return stepDetector;
    }

    @Override
    public void countStep() {
        this.timeOfLastPeak = this.timeOfThisPeak;
        this.timeOfThisPeak = System.currentTimeMillis();
        if (this.timeOfThisPeak - this.timeOfLastPeak <= 3000L){
            if (this.count < 9){
                this.count++;
            }else if (this.count == 9){
                this.count++;
                this.mCount+=this.count;
                notifyListener();
            }else {
                this.mCount++;
                notifyListener();
            }
        }else {
            this.count = 1;
        }
    }

    public void initListener(StepPassValueListener listener){
        this.mStepPassValueListener = listener;
    }

    private void notifyListener(){
        if (this.mStepPassValueListener != null){
            this.mStepPassValueListener.stepChanged(this.mCount);
        }
    }

    public void setSteps(int initValue){
        this.mCount = initValue;
        this.count = 0;
        this.timeOfLastPeak = 0;
        this.timeOfThisPeak = 0;
    }
}
