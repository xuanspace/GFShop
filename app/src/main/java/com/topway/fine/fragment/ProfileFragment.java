package com.topway.fine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.activity.LoginActivity;
import com.topway.fine.activity.PersonalInfoActivity;
import com.topway.fine.activity.RegisterActivity;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppContext;
import com.topway.fine.model.UserInfo;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的界面
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class ProfileFragment extends Fragment {

    private Activity context;

    @Bind(R.id.re_login) RelativeLayout re_login;
    @Bind(R.id.re_detail) RelativeLayout re_detail;
    @Bind(R.id.re_setting) RelativeLayout re_setting;
    @Bind(R.id.re_exit) RelativeLayout re_exit;
    @Bind(R.id.tv_username) TextView tv_username;
    @Bind(R.id.tv_binding_state) TextView tv_binding_state;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
        loadData();
    }

    void initView() {
        if (AppContext.isAuotLgoin()) {
            UserInfo user = AppContext.getInstance().getLoginUser();
            if (user != null) {
                tv_username.setText(user.getName());
                tv_binding_state.setText("已绑定");
            }
        }
    }

    private void initData() {

    }

    private void loadData() {

        re_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (AppContext.isAuotLgoin())
                    UIHelper.showPersonalInfo(context);
                else
                    showSignActivity();
            }
        });

        re_detail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showPersonalActvity();
            }
        });

        re_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                UIHelper.showSettingActivity(context);
            }
        });

        // 退出则清除登录信息
        re_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AppContext.getInstance().logout();
                showLoginActvity();
                context.finish();
            }
        });

    }

    private void showPersonalActvity() {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        startActivity(intent);
    }

    private void showRegisterActvity() {
        Intent intent = new Intent(context, RegisterActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_REGISTER);
    }

    private void showLoginActvity() {
        Intent intent = new Intent(context, LoginActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_LOGIN);
    }

    public void showSignActivity(){
        String userName = AppContext.getInstance().getProperty("user.name");
        if (StringUtils.isEmpty(userName)) {
            showRegisterActvity();
        }else{
            showLoginActvity();
        }
    }

    private void getSignResult(Intent data) {
        if (data != null) {
            String number = data.getStringExtra("phone");
            tv_username.setText(number);
            tv_binding_state.setText("已绑定");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_REGISTER:
                    getSignResult(data);
                    break;
                case APP.REQUEST_LOGIN:
                    getSignResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}