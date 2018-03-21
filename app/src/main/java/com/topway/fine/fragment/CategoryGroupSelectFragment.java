package com.topway.fine.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryGroupAdapter;
import com.topway.fine.base.BaseListFragment;
import com.topway.fine.model.Category;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 子分类：分类目录
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryGroupSelectFragment extends BaseListFragment {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.listView) ExpandableListView listView;

    private Activity context;
    private CategoryGroupAdapter adapter;
    private Category entity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category_select, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
        loadData();
    }

    public void initView() {
        initListView();
    }

    private void initListView() {
        listView.setGroupIndicator(null);

        // 监听组点击
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id)
            {

                if (adapter.isEmpty(groupPosition))
                {
                    return true;
                }
                return false;
            }
        });

        adapter = new CategoryGroupAdapter(context);
        listView.setAdapter(adapter);
    }

    public void initData() {
    }

    public void loadData() {

    }

}
