package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.EngineGroupAdapter;
import com.topway.fine.adapter.ProductListAdapter;
import com.topway.fine.adapter.SerieGroupAdapter;
import com.topway.fine.adapter.SerieSelectAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.base.ListActivity2;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.model.Part;
import com.topway.fine.model.Product;
import com.topway.fine.model.ProductDao;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 挖机机型选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SerieSelectActivity extends BaseActivity {

    @Bind(R.id.listView) LoadMoreListView listView;
    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;

    @Bind(R.id.re_manufacturer) RelativeLayout re_manufacturer;
    @Bind(R.id.tv_manufacturer) TextView tv_manufacturer;
    @Bind(R.id.tv_group) TextView tv_group;
    @Bind(R.id.tv_show) TextView tv_show;

    private SerieSelectAdapter adapter;
    private SerieSelectActivity context;
    private boolean isLoadAll;
    private boolean isMultiSelect;
    private long condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_select);
        ButterKnife.bind(this);
        setActivityTitle("机型");
        initView();
        initData();
    }

    private void initView() {
        context = this;
        isMultiSelect = false;
        re_manufacturer.setOnClickListener(new ClickListener());
        initListView();
        initSearchView();
    }

    private void initListView() {
        adapter = new SerieSelectAdapter(this, listView);
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
                    Serie item = (Serie) adapter.getItem(position);
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

        // 显示更多按钮
        tv_show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (condition == APP.SERIE_COMMON) {
                    condition = APP.SERIE_ALL;
                    tv_group.setText("全部机型");
                    tv_show.setText("显示常用机型");
                }else {
                    condition = APP.SERIE_COMMON;
                    tv_group.setText("常用机型");
                    tv_show.setText("显示全部机型");
                }
                adapter.setCondition(condition);
                adapter.refresh();
            }
        });
    }

    private void initData() {
        if (getOperation() == ENTITY_SELECT) {

        }

        // 缺省显示常用机型
        condition = APP.SERIE_COMMON;

        // 缺省显示挖机品牌
        Manufacturer item = adapter.getDefaultManufacturer();
        if (item != null) {
            tv_manufacturer.setText(item.getName());
            tv_manufacturer.setTag(item.getBrandId());

            // 加载挖机品牌下的机型
            adapter.initData();
        }
    }

    public void loadData() {

    }

    private void showManufacturerSelectActvity() {
        Intent intent = new Intent(this, ManufacturerSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_SERIE_SELECT);
    }

    private void getManufacturerSelectResult(Intent data) {
        if (data != null) {
            Manufacturer item = data.getParcelableExtra("entity");
            tv_manufacturer.setText(item.getName());
            tv_manufacturer.setTag(item.getId());
            adapter.setManufacturer(item);
            adapter.refresh();
        }
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_manufacturer:
                    showManufacturerSelectActvity();
                    break;
                case R.id.tv_show:
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_SERIE_SELECT:
                    getManufacturerSelectResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}