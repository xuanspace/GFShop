package com.topway.fine.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.topway.fine.R;
import com.topway.fine.adapter.BrandFilterAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.BrandFilterAsync;
import com.topway.fine.async.BrandHotAsync;
import com.topway.fine.async.HomeBrandAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.view.BrandListHeaderView;
import com.topway.fine.widget.Sidebar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @FileName BrandFilterFragment
 * @Description 品牌筛选界面
 * @Author linweixuan@gmail.com
 * @Date 2017-04-11 13:36
 * @Version V 1.0
 */

public class BrandFilterFragment extends BaseFragment {

    public static final String GET_HOT_BRAND = "1";
    public static final String GET_COMMON_BRAND = "2";

    @Bind(R.id.sidebar) Sidebar sidebar;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerContent;
    private GridView brandGirdView;

    private BrandListHeaderView listHeaderView;
    private StickyListHeadersListView stickyListHeadersListView;
    private BrandFilterAdapter brandFilterAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_banrd;
    }

    // 初始化各个View
    @Override
    public void initView() {
        initDrawerView();
        initIndexList();
        initGridList();
    }

    // 异步加载数据
    public void initData() {
        // 获取品牌数据
        new BrandHotAsync(this).execute(GET_HOT_BRAND);
        // 获取品牌数据
        new BrandFilterAsync(this).execute(GET_COMMON_BRAND);
    }

    // 初始化滑动栏
    public void initDrawerView() {
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerContent = (RelativeLayout) mActivity.findViewById(R.id.drawer_content);
    }

    // 品牌点击事件
    public void initGridList() {
        brandGirdView = listHeaderView.getGirdView();
        brandGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand item = (Brand)parent.getAdapter().getItem(position);
                postEvent(item);
                mDrawerLayout.closeDrawer(mDrawerContent);
            }
        });
    }

    /**
     * 初始化搜索索引列表
     */
    public void initIndexList() {

        //初始化索引表
        stickyListHeadersListView = (StickyListHeadersListView) mActivity.findViewById(R.id.lv_brnad);

        //设置头部的点击事件
        stickyListHeadersListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int position, long headerId, boolean currentlySticky) {
            }
        });

        //设置内容的点击事件
        stickyListHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long headerId) {
                Brand item = (Brand)brandFilterAdapter.getItem(position-1);
                mDrawerLayout.closeDrawer(mDrawerContent);
                postEvent(item);
            }
        });

        //设置头部改变的监听
        stickyListHeadersListView.setOnStickyHeaderChangedListener(new StickyListHeadersListView.OnStickyHeaderChangedListener() {
            @Override
            public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int position, long headerId) {
            }
        });

        // 添加索引表表头
        listHeaderView = new BrandListHeaderView(mActivity);
        stickyListHeadersListView.addHeaderView(listHeaderView);

        // 列表数据适配
        sidebar.setListView(stickyListHeadersListView.getWrappedList());
    }

    /**
     * 异步加载数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        if (type.equals(GET_HOT_BRAND)) {
            setHotBrandData((List)data);
        }
        if (type.equals(GET_COMMON_BRAND)) {
            setIndexBrandData((List)data);
        }
    }

    /**
     * 获取品牌数据完成后,设置索引数据
     * @param data 全部品牌数据
     */
    public void setHotBrandData(List data) {
        HomeBrandAdapter adapter = new HomeBrandAdapter(mActivity, data);
        brandGirdView.setAdapter(adapter);
        ViewGroup.LayoutParams linearParams = brandGirdView.getLayoutParams();
        linearParams.height = (int) (50*mActivity.getResources().getDisplayMetrics().density*(brandGirdView.getCount()/4));
        brandGirdView.setLayoutParams(linearParams);
    }

    /**
     * 获取品牌数据完成后,设置索引数据
     * @param data 全部品牌数据
     */
    public void setIndexBrandData(List data) {
        if (brandFilterAdapter == null) {
            brandFilterAdapter = new BrandFilterAdapter(mActivity);
        }
        brandFilterAdapter.setData(data);
        stickyListHeadersListView.setAdapter(brandFilterAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.tvCancel:
                mDrawerLayout.closeDrawer(mDrawerContent);
                break;
                */
        }
    }
}
