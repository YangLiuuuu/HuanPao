package com.yang.huanpao.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseFragment;
import com.yang.huanpao.bean.StepData;
import com.yang.huanpao.config.Const;
import com.yang.huanpao.ui.view.StepArcView;
import com.yang.huanpao.util.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yang on 2017/8/12.
 */

public class StepCountFragment extends BaseFragment{

    @BindView(R.id.step_count_arc_view)
    public StepArcView stepArcView;

    @BindView(R.id.week_plan_text)
    public TextView weekPlanText;

    @BindView(R.id.week_plan_distance_text)
    public TextView weekPlanDistanceText;

    @BindView(R.id.line_chart_view)
    public LineChartView lineChartView;

    @BindView(R.id.my_money_text)
    public TextView myMoneyText;

    @BindView(R.id.check_btn)
    public Button check;

    @BindView(R.id.circle_image)
    public CircleImageView circleImageView;

    private List<PointValue>mPointValues = new ArrayList<>();

    private List<AxisValue>mAxisValue = new ArrayList<>();

    private Tencent mTencent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_count_fragment,container,false);
        ButterKnife.bind(this,view);
//        EventBus.getDefault().register(this);

//        initData();
        initLineChart();
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent){
        if (messageEvent.getMessage().equals("数据库已更新")){
            Log.i("yangyang","Fragment接收到EventBus消息");
            initLineChart();
        }
    }

    //qq分享测试
    @OnClick(R.id.check_btn)
    public void onCheckBtnClick(View view){
        Bundle bundle = new Bundle();
        bundle.putString(SocialConstants.PARAM_TARGET_URL, "http://connect.qq.com/");
//分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	*/SUMMARY不能全为空，最少必须有一个是有值的。

        bundle.putString(SocialConstants.PARAM_TITLE, "我在测试");
//分享的图片URL
        bundle.putString(SocialConstants.PARAM_IMAGE_URL,
                "http://img3.cache.netease.com/photo/0005/2013-03-07/8PBKS8G400BV0005.jpg");
//分享的消息摘要，最长50个字
        bundle.putString(SocialConstants.PARAM_SUMMARY, "测试");
//手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(SocialConstants.PARAM_APPNAME, "??我在测试");
//标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, "星期几" + Const.Tencent_APP_ID);

        mTencent = Tencent.createInstance(Const.Tencent_APP_ID,getContext().getApplicationContext());

        mTencent.shareToQQ(getActivity(), bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent){
            mTencent.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void initLineChart() {
        mPointValues.clear();
        getAxisXLabel();//获取x坐标标注,及坐标数据
        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.chart_yellow));
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE)
                .setCubic(true)
                .setFilled(true)
                .setHasLabels(true)
                .setHasLines(true)
                .setHasPoints(true);
        lines.add(line);
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);

        //X轴部署
        Axis axisX = new Axis();//X轴
        axisX.setHasTiltedLabels(true)
                .setTextColor(Color.GREEN)
                .setTextSize(10)
                .setLineColor(Color.BLUE)
                .setValues(mAxisValue);
        axisX.setMaxLabelChars(9);
        lineChartData.setAxisXBottom(axisX);

        //Y轴部署
        Axis axisY = new Axis();
        axisY.setName("步数")
                .setTextSize(10)
        .setTextColor(Color.RED);
        lineChartData.setAxisYLeft(axisY);

        int colors[] = { 0xFF64AFE9 , 0xFF2590E2 };
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            lineChartView.setBackgroundDrawable(bg);
        } else {
            lineChartView.setBackground(bg);
        }
        lineChartView.setMaxZoom(2f);
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(lineChartData);
    }


    private void getAxisXLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String date = sdf.format(new Date());
        for (int i = 1 ; i < 7 ; i++){
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.add(Calendar.DAY_OF_MONTH,-i);
            String date = sdf.format(ca.getTime());
            List<StepData>list = DataSupport.where("today = ?",date).find(StepData.class);
            if (list.size() == 0 || list.isEmpty()){
                mPointValues.add(new PointValue(i,0));
            }else if (list.size() == 1){
                mPointValues.add(new PointValue(i,Integer.parseInt(list.get(0).getStep())));
            }else {
//                toast("访问数据库出错");
                mPointValues.add(new PointValue(i,Integer.parseInt(list.get(0).getStep())));
            }
            String value = date.substring(date.indexOf("-") + 1,date.lastIndexOf("-")) + "月" +
                    date.substring(date.lastIndexOf("-") + 1,date.length()) + "日";
            mAxisValue.add(new AxisValue(i).setLabel(value));
        }
    }

    private void initData() {
//        String planWalk_QTY = (String) sp.getParam("planWalk_QTY","7000");
        //设置当前步数为0
        stepArcView.setCurrentCount(7000,0);
    }

    public StepArcView getStepArcView(){
        return stepArcView;
    }
}
