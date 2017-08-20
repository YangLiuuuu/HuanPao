package com.yang.huanpao.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yang.huanpao.R;
import com.yang.huanpao.base.BaseFragment;
import com.yang.huanpao.ui.LoginActivity;
import com.yang.huanpao.ui.MainActivity;
import com.yang.huanpao.util.SharePreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yang on 2017/8/12.
 */

public class TempFragment extends BaseFragment {

    @BindView(R.id.logout)
    public Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temp_fragment,container,false);

        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.logout)
    public void onLogoutClick(View v){
        Log.i("yang","点击注销");
        SharePreferencesUtil.put(getContext(),"isLogin",false);
        ((MainActivity)getActivity()).startActivity(LoginActivity.class,null,true);
    }
}
