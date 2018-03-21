package com.topway.fine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.activity.ProductGatherActivity;
import com.topway.fine.activity.SearchActivity;
import com.topway.fine.activity.ViewPagerActivity;
import com.topway.fine.adapter.GalleryPagerAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.adapter.HomeCategoryAdapter;
import com.topway.fine.adapter.HomeProductAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.HomeAdvertiseAsync;
import com.topway.fine.async.HomeBrandAsync;
import com.topway.fine.async.HomeCategoryAsync;
import com.topway.fine.async.NewProductAsync;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Product;
import com.topway.fine.ui.loopviewpager.AutoLoopViewPager;
import com.topway.fine.ui.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.Bind;

/**
 * 主界面Fragment
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class HomeFragment extends BaseFragment {

    public static final String GET_NEW_PRODUCT = "1";

    @Bind(R.id.header) RelativeLayout header;
    @Bind(R.id.gv_hot) GridView categoryGirdView;
    @Bind(R.id.gv_new) GridView productGirdView;
    @Bind(R.id.gv_brand) GridView brandGirdView;
    @Bind(R.id.homeScrollView) ScrollView homeScrollView;
    @Bind(R.id.pager) AutoLoopViewPager adsPagerView;
    @Bind(R.id.indicator) CirclePageIndicator adsIndicator;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    // 初始化首页各个View
    @Override
    public void initView() {
    }

    // 初始化首页滚动设置
    @Override
    public void initEvent() {
        homeScrollView.smoothScrollTo(0, 0);
        homeScrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                /*
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    header.setVisibility(View.GONE);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    header.setVisibility(View.VISIBLE);
                    if (homeScrollView.getScrollY() == 0) {
                        header.setVisibility(View.GONE);
                    }
                }*/
                return false;
            }
        });

        // 品牌点击事件
        brandGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick())
                    return;

                Brand item = (Brand)parent.getAdapter().getItem(position);
                showBrandSearch(item);
            }
        });

        // 分类点击事件
        categoryGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick())
                    return;

                Category item = (Category)parent.getAdapter().getItem(position);
                showCategorySearch(item);
            }
        });

        // 产品点击事件
        productGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick())
                    return;

                Product item = (Product)parent.getAdapter().getItem(position);
                showProductDetail(item);
            }
        });
    }

    // 异步加载首页数据
    @Override
    public void initData() {
        // 获取广告图片
        new HomeAdvertiseAsync(this).execute();

        // 获取热门关键字
        new HomeCategoryAsync(this).execute();

        // 获取品牌数据
        new HomeBrandAsync(this).execute();

        // 获取产品数据
        //new HomeProductAsync(this).execute();
        new NewProductAsync(this).tag(GET_NEW_PRODUCT).execute();
    }

    // 初始化首页头部广告
    public void initAdvertise(List<String> data) {
        GalleryPagerAdapter adapter = new GalleryPagerAdapter(mActivity, data);
        adsPagerView.setAdapter(adapter);
        adsIndicator.setViewPager(adsPagerView);
        adsIndicator.setPadding(5, 5, 10, 5);
    }

    // 初始化首页品牌
    public void initBrands(List<Brand> data) {
        HomeBrandAdapter adapter = new HomeBrandAdapter(mActivity, data);
        brandGirdView.setAdapter(adapter);
        ViewGroup.LayoutParams linearParams = brandGirdView.getLayoutParams();
        linearParams.height = (int) (100*mActivity.getResources().getDisplayMetrics().density*(brandGirdView.getCount()/4));
        brandGirdView.setLayoutParams(linearParams);
    }

    // 初始化首页分类
    public void initCategory(List<Category> data) {
        HomeCategoryAdapter adapter = new HomeCategoryAdapter(mActivity, data);
        categoryGirdView.setAdapter(adapter);
        ViewGroup.LayoutParams linearParams = categoryGirdView.getLayoutParams();
        linearParams.height = (int) (100*mActivity.getResources().getDisplayMetrics().density*(categoryGirdView.getCount()/4));
        categoryGirdView.setLayoutParams(linearParams);
    }

    // 初始化首页产品(新发布)
    public void initNewProduct(List<Product> data) {
        HomeProductAdapter adapter = new HomeProductAdapter(mActivity, data);
        productGirdView.setAdapter(adapter);
        ViewGroup.LayoutParams linearParams = productGirdView.getLayoutParams();
        linearParams.height = (int) (102*mActivity.getResources().getDisplayMetrics().density*(productGirdView.getCount()/2));
        productGirdView.setLayoutParams(linearParams);
    }

    // 首页品牌搜索入口
    public void showBrandSearch(Brand item) {
        if (item != null) {
            item.setDefaultValue();
            Intent intent = new Intent(mActivity, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("search", "brand");
            bundle.putParcelable("entity", (Parcelable) item);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }

    // 首页分类搜索入口
    public void showCategorySearch(Category item) {
        if (item != null) {
            item.setDefaultValue();
            Intent intent = new Intent(mActivity, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("search", "category");
            bundle.putParcelable("entity", (Parcelable) item);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }

    // 首页产品详细入口
    public void showProductDetail(Product item) {
        if (item != null) {
            item.setDefaultValue();
            Intent intent = new Intent(mActivity, ProductGatherActivity.class);
            //Intent intent = new Intent(mActivity, ViewPagerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("operation", APP.ENTITY_SELECT);
            bundle.putParcelable("entity", (Parcelable) item);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }
    }

    /**
     * 异步加载数据处理
     * @param type 数据标签
     * @param data 数据对象
     */
    public void onAsyncData(Object type, Object data) {
        if (type.equals(GET_NEW_PRODUCT)) {
            initNewProduct((List)data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(mActivity).resumeTag(mActivity);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(mActivity).pauseTag(mActivity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(mActivity).cancelTag(mActivity);
    }
}