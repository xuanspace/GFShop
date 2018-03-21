package com.topway.fine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.ManufacturerAdapter;
import com.topway.fine.adapter.SerieSelectAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 挖机品牌选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ManufacturerSelectActivity extends BaseActivity {

    @Bind(R.id.listView) LoadMoreListView listView;
    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;
    @Bind(R.id.tv_show) TextView tv_show;

    private ManufacturerAdapter adapter;
    private ManufacturerSelectActivity context;
    private boolean isLoadAll;
    private boolean isMultiSelect;
    private long condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_select);
        ButterKnife.bind(this);
        setActivityTitle("品牌");
        initView();
        initData();
    }

    private void initView() {
        context = this;
        isMultiSelect = false;

        initListView();
        initSearchView();
    }

    private void initListView() {
        adapter = new ManufacturerAdapter(this, listView);
        adapter.setMultiSelect(isMultiSelect);
        listView.setAdapter(adapter);

        // 加载更多数据
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
            }
        });

        // 表项点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!isMultiSelect) {
                    Manufacturer item = (Manufacturer) adapter.getItem(position);
                    finishActivityResult(item);
                }
            }
        });
    }

    private void initSearchView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_search.clearFocus();

        // 显示搜索列表
        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                re_search.setVisibility(View.VISIBLE);
                //re_header.setVisibility(View.GONE);
            }
        });

        // 输入搜索关键字
        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    adapter.search("");
                }else{
                    adapter.search(tv_search.getText().toString());
                }
            }
        });

        tv_show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (condition == APP.BRAND_COMMON)
                    condition = APP.BRAND_ALL;
                else
                    condition = APP.BRAND_COMMON;
                adapter.setCondition(condition);
                adapter.refresh();
            }
        });
    }

    private void initData() {
        if (getOperation() == ENTITY_SELECT) {

        }

        // 缺省显示常用
        condition = APP.SERIE_COMMON;
        adapter.initData();
    }

    public void loadData() {

    }
}