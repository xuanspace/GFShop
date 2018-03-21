package com.topway.fine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 个人信息模块
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PersonalInfoActivity extends BaseActivity {

    @Bind(R.id.re_nickname) RelativeLayout re_nickname;
    @Bind(R.id.tv_nick_name) TextView tv_nick_name;

    PersonalInfoActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_nickname.setOnClickListener(new ClickListener());
    }

    private void initData() {

    }

    private void showNickModifyActvity() {
        Intent intent = new Intent(context, NickNameModifyActivity.class);
        startActivity(intent);
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_nickname:
                    showNickModifyActvity();
                    break;
            }
        }
    }

    private void getNickModifResult(Intent data) {
        if (data != null) {
            String name = data.getStringExtra("nick");
            tv_nick_name.setText(name);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_NICKNAME_MODIFY:
                    getNickModifResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
