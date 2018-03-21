package com.topway.fine.fragment;

import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.BrandFilterAdapter;
import com.topway.fine.adapter.CategoryFilterAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.adapter.SerieFilterAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.BrandFilterAsync;
import com.topway.fine.async.BrandHotAsync;
import com.topway.fine.async.CategoryPartAsync;
import com.topway.fine.async.SerieFilterAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;
import com.topway.fine.view.BrandListHeaderView;
import com.topway.fine.view.CategoryListHeaderView;
import com.topway.fine.widget.Sidebar;

import java.util.List;

import butterknife.Bind;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @FileName CategoryFilterFragment
 * @Description 分类筛选界面
 * @Author linweixuan@gmail.com
 * @Date 2017/4/13
 * @Version V 1.0
 */

public class CategoryFilterFragment extends BaseFragment {

    public static final String GET_PART_CATEGORY = "1";
    public static final String GET_SUB_CATEGORY = "2";

    @Bind(R.id.sidebar) Sidebar sidebar;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerContent;
    private TagFlowLayout flowlayout;

    private CategoryListHeaderView listHeaderView;
    private StickyListHeadersListView stickyListHeadersListView;
    private CategoryFilterAdapter subCategoryAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_banrd;
    }

    // 初始化各个View
    @Override
    public void initView() {
        initDrawerView();
        initIndexList();
        initTagList();
    }

    // 异步加载数据
    public void initData() {
        // 获取部件分类
        Long categoryTye = new Long(APP.CATEGORY_PART);
        new CategoryPartAsync(this).tag(GET_PART_CATEGORY).execute(categoryTye);
        // 获取部件子分类
        Long subCategoryTye = new Long(APP.CATEGORY_ENGINE_PART);
        new CategoryPartAsync(this).tag(GET_SUB_CATEGORY).execute(subCategoryTye);
    }

    // 初始化滑动栏
    public void initDrawerView() {
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerContent = (RelativeLayout) mActivity.findViewById(R.id.drawer_content);
    }

    // 部件的标签分类
    public void initTagList() {
        flowlayout = listHeaderView.getFlowLayout();
        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Category item = (Category)view.getTag();
                listHeaderView.setNextTtile(item.getName());
                Long subCategoryTye = item.getId();
                new CategoryPartAsync(mContext).tag(GET_SUB_CATEGORY).execute(subCategoryTye);
                return true;
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
                Category item = (Category)subCategoryAdapter.getItem(position-1);
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
        listHeaderView = new CategoryListHeaderView(mActivity);
        listHeaderView.setNextTtile("发动机件");
        stickyListHeadersListView.addHeaderView(listHeaderView);

        // 列表数据适配
        sidebar.setListView(stickyListHeadersListView.getWrappedList());
    }

    /**
     * 异步数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        List list = (data == null) ? null : (List)data;
        if (type.equals(GET_PART_CATEGORY)) {
            setPartCateoryData(list);
        }
        if (type.equals(GET_SUB_CATEGORY)) {
            setSubCategoryData(list);
        }
    }

    /**
     * 设置部件分类数据
     * @param data 全部品牌数据
     */
    public void setPartCateoryData(List data) {
        if (data == null) return;
        flowlayout.setAdapter(new TagAdapter<Category>(data) {
            @Override
            public View getView(FlowLayout parent, int position, Category item) {
                TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(
                        R.layout.item_category_tag, flowlayout, false);
                tv.setText(item.getName());
                tv.setTag(item);
                return tv;
            }
        });
    }

    /**
     * 设置子分类部件数据
     * @param data 全部品牌数据
     */
    public void setSubCategoryData(List data) {
        if (subCategoryAdapter == null) {
            subCategoryAdapter = new CategoryFilterAdapter(mActivity);
        }

        sidebar.setVisibility(View.VISIBLE);
        if (data == null || data.size() < 10) {
            sidebar.setVisibility(View.INVISIBLE);
        }

        subCategoryAdapter.setData(data);
        stickyListHeadersListView.setAdapter(subCategoryAdapter);
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
