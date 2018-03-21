package com.topway.fine.fragment;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.CategoryPartAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.model.Category;
import com.topway.fine.ui.UIHelper;

import java.util.List;

import butterknife.Bind;

/**
 * 分类页面左边总分类实现
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryFragment extends BaseFragment {

    @Bind(R.id.ev_search_text) TextView ev_search_text;
    @Bind(R.id.ev_search_input) EditText ev_search_input;
    @Bind(R.id.lv_category) ListView listview;

    private CategoryAdapter adapter;
    private CategoryIndexFragment subFragment;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    // 初始化各个View
    @Override
    public void initView() {
        ev_search_text.setVisibility(View.VISIBLE);
        ev_search_input.setVisibility(View.INVISIBLE);
    }

    // 初始化事件处理
    public void initEvent() {
        // 搜索输入点击跳转
        ev_search_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UIHelper.showSearchActivity(mActivity);
            }
        });

        // 列表点击事件
        listview.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (isDoubleClick())
                    return;
                Category item = (Category) adapter.getItem(position);
                onCategoryClick(item, position);
            }
        });
    }

    // 异步加载数据
    @Override
    public void initData() {
        Long type = new Long(APP.CATEGORY_PART);
        new CategoryPartAsync(this).execute(type);
        Long subtype = new Long(APP.CATEGORY_ENGINE_PART);
        showSubFramgment(subtype);
    }

    // 显示子分类页面
    public void showSubFramgment(Long type) {
        subFragment = new CategoryIndexFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_subcategory, subFragment);
        Bundle bundle = new Bundle();
        bundle.putLong(subFragment.TAG, type);
        subFragment.setArguments(bundle);
        fragmentTransaction.commit();
   }

    /**
     * 异步加载数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        setCategoryData((List)data);
    }

    // 设置分类数据
    public void setCategoryData(List<Category> data) {
        adapter = new CategoryAdapter(mActivity, data);
        listview.setAdapter(adapter);
    }

    // 分类选择处理
    public void onCategoryClick(Category item, int position) {
        adapter.setSelect(position);
        adapter.notifyDataSetChanged();
        Long type = item.getId();
        showSubFramgment(type);
   }

}