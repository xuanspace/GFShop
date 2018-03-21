package com.topway.fine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.topway.fine.R;
import com.topway.fine.adapter.DraweePagerAdapter;
import com.topway.fine.adapter.GalleryPagerAdapter;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.async.HomeBrandAsync;
import com.topway.fine.async.ProductPhotoAsync;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.fragment.ProductDetailsFragment;
import com.topway.fine.fragment.ProductParamFragment;
import com.topway.fine.fragment.ProductValueFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Product;
import com.topway.fine.ui.loopviewpager.AutoLoopViewPager;
import com.topway.fine.ui.photoview.MultiTouchViewPager;
import com.topway.fine.ui.tabstrip.PagerSlidingTabStrip;
import com.topway.fine.ui.viewpagerindicator.CirclePageIndicator;
import com.topway.fine.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static com.topway.fine.R.id.iv_picture;
import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

/**
 * 产品基本信息
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class ProductGatherActivity extends BaseActivity {

    @Bind(R.id.iv_back) ImageView btnBack;
    @Bind(R.id.btnShare) Button btnShare;
    @Bind(R.id.tv_title) TextView headTitle;

    // 产品概要信息
    @Bind(R.id.tv_summary_title) TextView tv_summary_title;
    @Bind(R.id.tv_summary_price) TextView tv_summary_price;
    @Bind(R.id.tv_summary_quality) TextView tv_summary_quality;
    @Bind(R.id.tv_summary_update) TextView tv_summary_update;
    @Bind(R.id.tv_summary_location) TextView tv_summary_location;

    // 产品联系人信息
    @Bind(R.id.tv_contact) TextView tv_contact;
    @Bind(R.id.tv_address) TextView tv_address;

    // 产品详细信息
    @Bind(R.id.tv_product_device) TextView tv_product_device;
    @Bind(R.id.tv_product_brand) TextView tv_product_brand;
    @Bind(R.id.tv_product_serie) TextView tv_product_serie;
    @Bind(R.id.tv_product_engine) TextView tv_product_engine;
    @Bind(R.id.tv_product_partname) TextView tv_product_partname;
    @Bind(R.id.tv_product_category) TextView tv_product_category;
    @Bind(R.id.tv_product_price) TextView tv_product_price;
    @Bind(R.id.tv_product_date) TextView tv_product_date;
    @Bind(R.id.tv_product_code) TextView tv_product_code;
    @Bind(R.id.tv_product_number) TextView tv_product_number;
    @Bind(R.id.tv_product_desc) TextView tv_product_desc;

    private ProductGatherActivity context;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_gather);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;
        // 初始化标题栏
        initActionBar();
    }

    // 初始化标题栏
    public void initActionBar() {
        headTitle.setText("产品");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initPhotoViewPage(List<String> imageList) {
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        MultiTouchViewPager viewPager = (MultiTouchViewPager) findViewById(R.id.view_pager);
        DraweePagerAdapter adapter = new DraweePagerAdapter(this, imageList);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
    }

    public void initImageViewPage(List<String> imageList) {
        SimpleDraweeView image =  (SimpleDraweeView)findViewById(R.id.iv_picture);
        Uri uri = Uri.parse(imageList.get(0));
        image.setImageURI(uri);

        AutoLoopViewPager imagePager =  (AutoLoopViewPager)findViewById(R.id.view_pager);
        CirclePageIndicator indicator =  (CirclePageIndicator)findViewById(R.id.indicator);
        GalleryPagerAdapter galleryAdapter = new GalleryPagerAdapter(context);
        galleryAdapter.setImages(imageList);
        imagePager.setAdapter(galleryAdapter);
        indicator.setViewPager(imagePager);
        indicator.setPadding(5, 5, 10, 5);
    }

    // 初始化产品图片
    public void initProductImages(List<String> imageList) {
        initImageViewPage(imageList);
    }

    void initData() {
        if (getOperation() == ENTITY_SELECT) {
            // 获取产品图片
            product = getParameter(Product.class);
            if (product != null) {
                new ProductPhotoAsync(this).execute(product.getId());
                initProductSummaryInfo();
                initProductDetailInfo();
            }
        }
    }

    // 初始化产品信息
    void initProductSummaryInfo() {
        if (product != null) {
            tv_summary_title.setText(product.getName());
            tv_summary_price.setText(product.getPrice().toString());
        }
    }

    // 初始化产品信息
    void initProductDetailInfo() {
        if (product != null) {
            //tv_product_device.setText("");
            tv_product_partname.setText(product.getName());
            tv_product_price.setText(product.getPrice().toString());
            tv_product_code.setText(product.getCode());
            tv_product_number.setText(product.getNumber());
            tv_product_desc.setText(product.getDescription());

            if (product.getBrand() != null) {
                tv_product_brand.setText(product.getBrand().getName());
            }
            if (product.getSerie() != null) {
                tv_product_serie.setText(product.getSerie().getName());
            }
            if (product.getEngine() != null) {
                tv_product_engine.setText(product.getEngine().getName());
            }
            if (product.getCategory() != null) {
                tv_product_category.setText(product.getCategory().getName());
            }
            if (product.getUptime() != null) {
                String datetime = StringUtils.toNYR(product.getUptime().longValue()*1000);
                tv_product_date.setText(datetime);
            }
        }
    }

    private void initEvent() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 是否只有已登录用户才能打开分享选择页
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //imagePager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //imagePager.stopAutoScroll();
    }

}
