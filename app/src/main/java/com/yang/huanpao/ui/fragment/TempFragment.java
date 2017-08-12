package com.yang.huanpao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseFragment;

/**
 * Created by yang on 2017/8/12.
 */

public class TempFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.temp_fragment,container,false);
    }
}
