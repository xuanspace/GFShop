package com.topway.fine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.topway.fine.R;
import com.topway.fine.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改个人的Nick Name
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class NickNameModifyActivity  extends BaseActivity {

    @Bind(R.id.nick_name_edittext) EditText tv_name;

    NickNameModifyActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname_modify);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;

    }

    private void initData() {

    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_nickname:

                    break;
            }
        }
    }

}

