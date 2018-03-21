package com.topway.fine.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.topway.fine.R;
import com.topway.fine.adapter.BrandFilterAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.adapter.SerieFilterAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.BrandFilterAsync;
import com.topway.fine.async.BrandHotAsync;
import com.topway.fine.async.SerieFilterAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.utils.StringUtils;
import com.topway.fine.view.BrandListHeaderView;
import com.topway.fine.widget.Sidebar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @FileName BrandFilterFragment
 * @Description 机型筛选界面
 * @Author linweixuan@gmail.com
 * @Date 2017-04-11 13:36
 * @Version V 1.0
 */
public class SerieFilterFragment extends BaseFragment {

    public static final String GET_BRAND = "1";
    public static final String GET_SERIE = "2";

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerContent;

    private GridView brandGirdView;
    private LoadMoreListView serieListView;
    private BrandListHeaderView listHeaderView;
    private SerieFilterAdapter serieFilterAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_serie;
    }

    // 初始化各个视图
    public void initView() {
        initDrawerView();
        initListView();
        initGridView();
    }

    // 初始化滑动栏
    public void initDrawerView() {
        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        mDrawerContent = (RelativeLayout) mActivity.findViewById(R.id.drawer_content);
    }

    // 机型品牌网格
    public void initGridView() {
        brandGirdView = listHeaderView.getGirdView();
        brandGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand item = (Brand)parent.getAdapter().getItem(position);
                String brandId = String.valueOf(item.getId());
                String condition = String.valueOf(APP.SERIE_COMMON);
                String pageIndex = String.valueOf(0);
                new SerieFilterAsync(mContext).execute(GET_SERIE, brandId, condition, pageIndex);
                listHeaderView.setNextTtile(item.getName());
            }
        });
    }

    // 初始化机型列表
    public void initListView() {
        // 添加索引表表头
        serieListView = (LoadMoreListView) mActivity.findViewById(R.id.lv_serie);
        listHeaderView = new BrandListHeaderView(mActivity);
        listHeaderView.setNextTtile("小松");
        listHeaderView.setNextBarColor();
        serieListView.addHeaderView(listHeaderView);
        serieListView.hideFooterView();

        // 列表项点击事件
        serieListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Serie item = (Serie) serieFilterAdapter.getItem(position-1);
                postEvent(item);
                mDrawerLayout.closeDrawer(mDrawerContent);
            }
        });
    }

    // 异步加载机型数据
    public void initData() {
        // 获取品牌数据
        new BrandHotAsync(this).execute(GET_BRAND);

        // 获取机型数据
        String brandId = String.valueOf(APP.BRAND_KOMATSU);
        String condition = String.valueOf(APP.SERIE_COMMON);
        String pageIndex = String.valueOf(0);
        new SerieFilterAsync(this).execute(GET_SERIE, brandId, condition, pageIndex);
    }

    /**
     * 异步加载数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        if (type.equals(GET_BRAND)) {
            setBrandData((List)data);
        }
        if (type.equals(GET_SERIE)) {
            setSerieData((List)data);
        }
    }

    /**
     * 设置机型品牌数据
     * @param data 全部品牌数据
     */
    public void setBrandData(List data) {
        HomeBrandAdapter adapter = new HomeBrandAdapter(mActivity, data);
        brandGirdView.setAdapter(adapter);
        ViewGroup.LayoutParams linearParams = brandGirdView.getLayoutParams();
        linearParams.height = (int) (50*mActivity.getResources().getDisplayMetrics().density*(brandGirdView.getCount()/4));
        brandGirdView.setLayoutParams(linearParams);
    }

    /**
     * 设置机型数据适配
     * @param data 全部品牌数据
     */
    public void setSerieData(List data) {
        if (serieFilterAdapter == null) {
            serieFilterAdapter = new SerieFilterAdapter(mActivity);
            serieListView.setAdapter(serieFilterAdapter);
        }
        serieFilterAdapter.setData(data);
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
