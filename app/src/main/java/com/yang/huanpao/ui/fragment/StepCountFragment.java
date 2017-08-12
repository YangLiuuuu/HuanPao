package com.yang.huanpao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseFragment;
import com.yang.huanpao.ui.view.StepArcView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yang on 2017/8/12.
 */

public class StepCountFragment extends BaseFragment {

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_count_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
}
