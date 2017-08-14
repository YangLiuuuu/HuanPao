package com.yang.huanpao.ui.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.yang.huanpao.R;

/**
 * Created by yang on 2017/8/10.
 */

public class StepArcView extends View {
    private Paint mPaint = new Paint();

    /**
     * 圆弧的宽度
     */
    private float borderWidth = dip2px(14);

    /**
     * 开始绘制圆弧的角度
     */
    private float startAngle = 135;

    /**
     * 终点对应的角度和起始点对应的角度的夹角
     */
    private float angleLength = 270;

    private float numberTextSize = 35;

    private float currentAngleLength = 0;

    /**
     * 动画时长
     */
    private int animationLength = 3000;

    /**
     * 步数
     */
    private String stepNumber = "0";

    public StepArcView(Context context) {
        super(context);
    }

    public StepArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StepArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public StepArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //中心点的x坐标
        float centerX = getWidth()/2;

        /** 指定的外轮廓矩形区域*/
        RectF rectF = new RectF(0 + borderWidth,borderWidth,2 * centerX - borderWidth,2 * centerX - borderWidth);

        /**
         * 绘制整体的黄色圆弧
         */
        drawArcYellow(canvas,rectF);

        /**
         * 绘制当前进度的红色圆弧
         */
        drawArcRed(canvas,rectF);
        /**
         * ：绘制当前前进步数的数字
         */
        drawTextNumber(canvas,centerX);

        /**
         * 绘制“步数”文字
         */
        drawTextStepString(canvas,centerX);

    }

    /**
     * 绘制黄色圆弧
     * @param canvas
     * @param rectF
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawArcYellow(Canvas canvas, RectF rectF) {
        mPaint.setColor(getResources().getColor(R.color.yellow,null));
        //结合处为圆弧
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置画笔的样式Round SQUARE分别为圆形方形
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(borderWidth);

        canvas.drawArc(rectF,startAngle,angleLength,false,mPaint);
    }


    /**
     * 第二步:绘制当前进度的红色圆弧
     * @param canvas
     * @param rectF
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawArcRed(Canvas canvas, RectF rectF) {
        mPaint.reset();
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(getResources().getColor(R.color.red,null));

        canvas.drawArc(rectF,startAngle,currentAngleLength,false,mPaint);
    }

    /**
     * 绘制前进步数的数字
     * @param canvas
     * @param centerX
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawTextNumber(Canvas canvas, float centerX) {
        mPaint.reset();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(numberTextSize);
        Typeface font = Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
        mPaint.setTypeface(font);//字体风格
        mPaint.setColor(getResources().getColor(R.color.red,null));
        Rect bounds = new Rect();
        mPaint.getTextBounds(stepNumber,0,stepNumber.length(),bounds);
        canvas.drawText(stepNumber,centerX,getHeight()/2+bounds.height()/2,mPaint);
    }

    /**
     * 绘制“步数”文字
     * @param canvas
     * @param centerX
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void drawTextStepString(Canvas canvas, float centerX) {
        Paint vTextPaint = new Paint();
        vTextPaint.setTextSize(dip2px(14));
        vTextPaint.setTextAlign(Paint.Align.CENTER);
        vTextPaint.setAntiAlias(true);//抗锯齿功能
        vTextPaint.setColor(getResources().getColor(R.color.grey,null));
        String stepString = "今日步数";
        Rect bounds = new Rect();
        vTextPaint.getTextBounds(stepString, 0, stepString.length(), bounds);
        canvas.drawText(stepString, centerX, getHeight() / 2 + bounds.height() + getFontHeight(numberTextSize), vTextPaint);
    }


    /**
     * 获取当前步数数字的高度
     * @param numberTextSize
     * @return
     */
    private int getFontHeight(float numberTextSize) {
        mPaint.reset();
        mPaint.setTextSize(numberTextSize);
        Rect boundsNumber = new Rect();
        mPaint.getTextBounds(stepNumber,0,stepNumber.length(),boundsNumber);
        return boundsNumber.height();
    }

    public int dip2px(float dip){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f *(dip >= 0 ? 1 : -1));
    }

    /**
     * 所走的步数进度
     * @param totalStepNum 设置的步数
     * @param currentCount 所走的步数
     */
    public void setCurrentCount(int totalStepNum,int currentCount){
        if (currentCount > totalStepNum){
            currentCount = totalStepNum;
        }

        float scalePrevious = (float) Integer.valueOf(stepNumber)/totalStepNum;
        float previousAngleLength = scalePrevious * angleLength;

        float scale = (float)currentCount/totalStepNum;
        float currentAngleLength = scale * angleLength;

        setAnimation(previousAngleLength,currentAngleLength,animationLength);

        stepNumber = String.valueOf(currentCount);
        setTextSize(currentCount);
    }

    private void setAnimation(float start,float end,int length){
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(start,end);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngleLength);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //每次在初始值和结束值之间产生一个平滑过渡值，逐步去更新数字
                currentAngleLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }

    /**
     * 设置文本大小，防止数字特别大后放不下，将字体大小动态设置
     * @param num
     */
    public void setTextSize(int num) {
        String s = String.valueOf(num);
        int length = s.length();
        if (length <= 4){
            numberTextSize = dip2px(50);
        }else if (length > 4 && length <= 6){
            numberTextSize = dip2px(40);
        }else if (length > 6 && length <= 8){
            numberTextSize = dip2px(30);
        }else if (length > 8){
            length = dip2px(25);
        }
    }
}
