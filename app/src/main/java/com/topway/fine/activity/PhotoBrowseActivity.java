package com.topway.fine.activity;

import android.view.View;

import com.anbetter.log.MLog;
import com.facebook.fresco.helper.photoview.PictureBrowseActivity;
import com.topway.fine.R;

/**
 * 查看大图
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class PhotoBrowseActivity extends PictureBrowseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_photo_browse;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        findViewById(R.id.rl_top_deleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLog.i("用户点击了删除按钮");

            }
        });
    }

}
