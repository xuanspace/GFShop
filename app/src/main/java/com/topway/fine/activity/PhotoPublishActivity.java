package com.topway.fine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.FunctionAdapter;
import com.topway.fine.adapter.PhotoPublishAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.AlbumPhoto;
import com.topway.fine.model.CategoryGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布图片
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhotoPublishActivity extends BaseActivity {

    private static final String TAG = PhotoPublishAdapter.class.getSimpleName();
    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_INDEX = "extra_index";

    @Bind(R.id.tv_comment) EditText comment;
    @Bind(R.id.gv_photo) GridView gridView;

    private PhotoPublishActivity context;
    private PhotoPublishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_publish);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        adapter = new PhotoPublishAdapter(context);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new GridViewListener());
    }

    private void initData() {

    }

    private class GridViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.isAddMore(position)) {
                showPhotoSelectActivity();
            }
            else{
                showPhotoPreviewActivity();
            }
        }
    }

    public void showPhotoSelectActivity() {
        Intent intent = new Intent(this, PhotoSelectActivity.class);
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_SELECT);
    }

    public void showPhotoPreviewActivity() {
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_DATA, adapter.getData());
        intent.putExtra(EXTRA_INDEX, adapter.getSelectedPosition());
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_PREVIEW);
    }

    public void getPhotoSelectResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.addData(images);
        }
    }

    public void getPhotoPreviewResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.setData(images);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_PHOTO_SELECT:
                    getPhotoSelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_PREVIEW:
                    getPhotoPreviewResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
