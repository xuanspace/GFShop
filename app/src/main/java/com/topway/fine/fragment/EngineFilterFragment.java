package com.topway.fine.fragment;

import android.annotation.SuppressLint;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.topway.fine.R;
import com.topway.fine.adapter.EngineGroupAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.adapter.SerieFilterAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.BrandHotAsync;
import com.topway.fine.async.SerieFilterAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Engine;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.view.BrandListHeaderView;

import java.util.List;

import butterknife.Bind;

/**
 * @FileName EngineFilterFragment
 * @Description 发动机型号筛选界面
 * @Author linweixuan@gmail.com
 * @Date 2017-04-11 13:36
 * @Version V 1.0
 */

public class EngineFilterFragment extends BaseFragment {

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerContent;

    private ExpandableListView engineListView;
    private EngineGroupAdapter engineAadapter;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_engine;
    }

    // 初始化各个视图
    public void initView() {
        initDrawerView();
        initListView();
    }

    // 初始化滑动栏
    public void initDrawerView() {
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerContent = (RelativeLayout) mActivity.findViewById(R.id.drawer_content);
    }

    // 初始化索引列表
    public void initListView() {
        engineListView = (ExpandableListView) mActivity.findViewById(R.id.listView);
        engineListView.setGroupIndicator(null);

        // 监听组点击
        engineListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                if (engineAadapter.isEmpty(groupPosition))
                    return true;
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        engineListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Engine item = (Engine)engineAadapter.getChild(groupPosition,childPosition);
                postEvent(item);
                mDrawerLayout.closeDrawer(mDrawerContent);
                return true;
            }
        });

        engineAadapter = new EngineGroupAdapter(mActivity, engineListView);
        engineAadapter.setGroupReosure(R.layout.expend_engine_group_small);
        engineAadapter.setItemReosure(R.layout.list_engine_item_small);
        engineListView.setAdapter(engineAadapter);
    }

    // 异步加载数据
    public void initData() {
        // 获取品牌数据
        //new BrandHotAsync(this).execute();
        engineAadapter.initData();
    }

    /**
     * 异步加载数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        List list = (data == null) ? null : (List)data;
        setEngineData(list);
    }

    /**
     * 设置发动机型号数据
     * @param data 全部品牌数据
     */
    public void setEngineData(List data) {
        if (engineAadapter == null) {
            engineAadapter = new EngineGroupAdapter(mActivity, engineListView);
            engineListView.setAdapter(engineAadapter);
        }
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
