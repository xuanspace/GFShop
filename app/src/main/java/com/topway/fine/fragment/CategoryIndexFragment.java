package com.topway.fine.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.topway.fine.R;
import com.topway.fine.activity.SearchActivity;
import com.topway.fine.adapter.CategoryIndexAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.widget.Sidebar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 分类索引页面实现
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryIndexFragment extends BaseFragment {

    public static final String TAG = "CategoryIndexFragment";

    @Bind(R.id.sidebar) Sidebar sidebar;
    @Bind(R.id.listView) ListView listView;

    private CategoryIndexAdapter adapter;
    private Category category;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category_index;
    }

    @Override
    public void initView() {
        // 列表数据适配
        sidebar.setListView(listView);
        adapter = new CategoryIndexAdapter(mActivity);
        adapter.hideCategoryIcon();
        listView.setAdapter(adapter);
    }

    @Override
    public void initEvent() {
        // 列表项点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick())
                    return;
                Category item = (Category) adapter.getItem(position);
                showSearchActivity(item);
            }
        });
    }

    @Override
    public void initData() {
        Long categoryId = getArguments().getLong(TAG);
        if (categoryId != null) {
            loadData(categoryId.longValue());
        }
    }

    public void loadData(long pid) {
        category = DatabaseHelper.instance().getCategory(pid);
        if (category != null) {
            adapter.loadData(pid);
        }else{
            adapter.loadData(APP.CATEGORY_ENGINE_PART);
        }
    }

    public void showSearchActivity(Category category){
        if (category != null) {
            category.setDefaultValue();
            Intent intent = new Intent(mActivity, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("search", "category");
            bundle.putParcelable("entity", (Parcelable)category);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }

}