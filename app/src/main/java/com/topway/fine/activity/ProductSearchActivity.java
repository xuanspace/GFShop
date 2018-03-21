package com.topway.fine.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import com.topway.fine.R;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.fragment.ProductDetailsFragment;
import com.topway.fine.fragment.ProductParamFragment;
import com.topway.fine.fragment.ProductValueFragment;
import com.topway.fine.ui.tabstrip.PagerSlidingTabStrip;

/**
 * 产品搜索界面
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class ProductSearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_product_show);
    }

}
