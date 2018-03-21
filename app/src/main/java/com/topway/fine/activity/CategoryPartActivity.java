package com.topway.fine.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.BrandSelectAdapter;
import com.topway.fine.adapter.CategoryPartAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 挖机配件第一层分类选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryPartActivity extends BaseActivity {

    @Bind(R.id.listView) LoadMoreListView listView;

    private CategoryPartActivity context;
    private CategoryPartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_part);
        setActivityTitle("选择分类");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;

        // 获取要选择分类
        if (getOperation() == ENTITY_SELECT) {
        }

        adapter = new CategoryPartAdapter(this, listView);
        listView.setAdapter(adapter);

        // 加载更多
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
            }
        });

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Category item = (Category) adapter.getItem(position);
                finishActivityResult(item);
            }
        });
    }

    public void initData() {
        adapter.initData();
    }
}