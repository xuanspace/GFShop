package com.topway.fine.activity;

import android.os.Bundle;
import android.view.View;
import com.topway.fine.R;
import com.topway.fine.ui.swipebacklayout.SwipeBackActivity;

/**
 * Created by lwx on 2016/2/29.
 */

public class SettingActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setActivityTitle("设置");

    }

}