package com.topway.fine.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;

import com.topway.fine.R;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.fragment.ProductDetailsFragment;
import com.topway.fine.fragment.ProductImageFragment;
import com.topway.fine.fragment.ProductParamFragment;
import com.topway.fine.fragment.ProductValueFragment;
import com.topway.fine.ui.tabstrip.PagerSlidingTabStrip;

/**
 * 产品展示界面
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class ProductShowAcitivity extends BaseActivity {
    private PagerSlidingTabStrip viewTabs;
    private ViewPager viewPager;
    private DisplayMetrics dm;

    private ProductDetailsFragment detailFragment;
    private ProductParamFragment paramFragment;
    private ProductValueFragment valueFragment;

    String[] viewTitles = { "图文详情", "产品参数", "产品评价" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_product_show);
        dm = getResources().getDisplayMetrics();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),viewTitles));
        viewTabs.setViewPager(viewPager);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        String[] titles;
        public ViewPagerAdapter(FragmentManager fm, String[] viewTitles) {
            super(fm);
            titles = viewTitles;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (detailFragment == null) {
                        detailFragment = new ProductDetailsFragment();
                    }
                    return detailFragment;
                case 1:
                    if (paramFragment == null) {
                        paramFragment = new ProductParamFragment();
                    }
                    return paramFragment;
                case 2:
                    if (valueFragment == null) {
                        valueFragment = new ProductValueFragment();
                    }
                    return valueFragment;
                default:
                    return null;
            }
        }
    }

}
