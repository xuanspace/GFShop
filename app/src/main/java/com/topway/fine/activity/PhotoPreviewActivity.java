package com.topway.fine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.PhotoPreviewAdapter;
import com.topway.fine.adapter.PhotoPublishAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.AlbumPhoto;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片预览
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhotoPreviewActivity  extends BaseActivity {

    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_INDEX = "extra_index";

    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.cb_select) CheckBox selectCheckBox;
    @Bind(R.id.tv_title) TextView title;
    @Bind(R.id.iv_back) ImageView back;
    @Bind(R.id.tv_confirm) TextView confirm;

    private PhotoPreviewActivity context;
    private PhotoPreviewAdapter adapter;
    private int currentIndex;
    private ArrayList<String> previewImages;
    private ArrayList<String> uncheckImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        uncheckImages = new ArrayList<String>();
        currentIndex = getIntent().getIntExtra(EXTRA_INDEX, 1);
        previewImages = getIntent().getStringArrayListExtra(EXTRA_DATA);
    }

    private void setActionBar() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        confirm.setEnabled(true);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImages.removeAll(uncheckImages);
                setResult(RESULT_OK, new Intent().putExtra(EXTRA_DATA, previewImages));
                finish();
            }
        });

        title.setText(String.format(getString(R.string.page_number_format), 1, previewImages.size()));
    }

    private void initView() {
        context = this;
        setActionBar();
        initViewPager();
        initCheckView();
    }

    private void initViewPager() {
        adapter = new PhotoPreviewAdapter(context, previewImages);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectCheckBox.setChecked(!uncheckImages.contains(previewImages.get(position)));
                title.setText(String.format(getString(R.string.page_number_format), position + 1, previewImages.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCheckView() {
        selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectCheckBox.isChecked()) {
                    uncheckImages.add(previewImages.get(viewPager.getCurrentItem()));
                } else {
                    uncheckImages.remove(previewImages.get(viewPager.getCurrentItem()));
                }
            }
        });
    }

}