package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.ProductListAdapter;
import com.topway.fine.adapter.SerieSelectAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Part;
import com.topway.fine.model.Product;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 部件快速Activity
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class PartQuickActivity  extends BaseActivity {

    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;

    @Bind(R.id.re_serie) RelativeLayout re_serie;
    @Bind(R.id.re_part) RelativeLayout re_part;
    @Bind(R.id.ll_serie) LinearLayout ll_serie;
    @Bind(R.id.ll_engine) LinearLayout ll_engine;
    @Bind(R.id.ll_part) LinearLayout ll_part;
    @Bind(R.id.ll_brand) LinearLayout ll_brand;

    @Bind(R.id.tv_serie) TextView tv_serie;
    @Bind(R.id.tv_engine) TextView tv_engine;
    @Bind(R.id.tv_part) TextView tv_part;
    @Bind(R.id.tv_brand) TextView tv_brand;

    @Bind(R.id.tv_switch) TextView tv_switch;
    @Bind(R.id.tv_part_tip) TextView tv_part_tip;
    @Bind(R.id.tv_advance) TextView tv_advance;

    @Bind(R.id.iv_clean_serie) ImageView iv_clean_serie;
    @Bind(R.id.iv_clean_engine) ImageView iv_clean_engine;
    @Bind(R.id.iv_clean_category) ImageView iv_clean_category;
    @Bind(R.id.iv_clean_brand) ImageView iv_clean_brand;


    @Bind(R.id.listView) LoadMoreListView listView;

    private PartQuickActivity context;
    private ProductListAdapter adapter;
    private boolean isShowAdvance;

    /*
    private Brand brand;
    private Serie serie;
    private Engine engine;
    private Category category;
    private Part detail;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_quick);
        this.setActivityTitle("部件");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        isShowAdvance = false;

        ll_serie.setOnClickListener(new ClickListener());
        ll_engine.setOnClickListener(new ClickListener());
        ll_part.setOnClickListener(new ClickListener());
        ll_brand.setOnClickListener(new ClickListener());
        tv_switch.setOnClickListener(new ClickListener());
        tv_advance.setOnClickListener(new ClickListener());

        iv_clean_serie.setOnClickListener(new ClickListener());
        iv_clean_engine.setOnClickListener(new ClickListener());
        iv_clean_category.setOnClickListener(new ClickListener());
        iv_clean_brand.setOnClickListener(new ClickListener());

        initListView();
        initSearchView();
        hideSelecView();
    }

    public void initData() {
        adapter.initData();
    }

    private void initListView() {
        adapter = new ProductListAdapter(this, listView);
        adapter.setMultiSelect(false);
        listView.setAdapter(adapter);

        // 加载更多数据
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
            }
        });

        // 表项点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Product item = (Product) adapter.getItem(position);
                if (item != null) {
                    //showProductDetailActvity(item);
                    showProductGatherActvity(item);
                }
            }
        });
    }

    private void hideSelecView() {
        ll_serie.setVisibility(View.GONE);
        ll_engine.setVisibility(View.GONE);
        ll_part.setVisibility(View.GONE);
        ll_brand.setVisibility(View.GONE);
        re_serie.setVisibility(View.GONE);
        re_part.setVisibility(View.GONE);
    }

    private void showSelecView() {
        //ll_serie.setVisibility(View.GONE);
        //ll_engine.setVisibility(View.VISIBLE);
        ll_part.setVisibility(View.VISIBLE);
        ll_brand.setVisibility(View.VISIBLE);
        re_serie.setVisibility(View.VISIBLE);
        re_part.setVisibility(View.GONE);
    }

    private void showAdanceView() {
        if (isShowAdvance) {
            hideSelecView();
            isShowAdvance = false;
        }else{
            isShowAdvance = true;
            showSelecView();
            if (tv_switch.getText().toString().contains("发动机")) {
                ll_serie.setVisibility(View.GONE);
                ll_engine.setVisibility(View.VISIBLE);
            }else{
                ll_serie.setVisibility(View.VISIBLE);
                ll_engine.setVisibility(View.GONE);
            }
        }
    }

    private void initSearchView() {
        // 显示搜索列表
        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (re_search.getVisibility() == View.GONE){
                    re_search.setVisibility(View.VISIBLE);
                    //hideSelecView();
                }else{
                    re_search.setVisibility(View.GONE);
                    //showSelecView();
                    //re_header.setVisibility(View.GONE);
                }
            }
        });

        // 输入搜索关键字
        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    adapter.search("");
                }else{
                    adapter.search(tv_search.getText().toString());
                }
            }
        });
    }

    private void refresh() {
        Serie serie = (Serie) tv_serie.getTag();
        Engine engine = (Engine) tv_engine.getTag();
        Category category = (Category) tv_part.getTag();
        Brand brand = (Brand) tv_brand.getTag();

        if (ll_engine.getVisibility() == View.GONE)
            adapter.setEngine(null);
        else
            adapter.setEngine(engine);

        if (ll_serie.getVisibility() == View.GONE)
            adapter.setSerie(null);
        else
            adapter.setSerie(serie);

        if (ll_brand.getVisibility() == View.GONE)
            adapter.setBrand(null);
        else
            adapter.setBrand(brand);

        if (ll_part.getVisibility() == View.GONE)
            adapter.setCategory(null);
        else
            adapter.setCategory(category);

        adapter.refresh();
    }

    /**
     * 显示产品详细的信息页面(Tab形式)
     * @param item  产品实例
     */
    private void showProductGatherActvity(Product item) {
        Intent intent = new Intent(this, ProductGatherActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        bundle.putParcelable("entity", (Parcelable)item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 显示产品详细的信息页面(Tab形式)
     * @param item  产品实例
     */
    private void showProductDetailActvity(Product item) {
        Intent intent = new Intent(this, ProductShowAcitivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        bundle.putParcelable("entity", (Parcelable)item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void showSerieSelectActvity() {
        Intent intent = new Intent(this, SerieSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_SERIE_SELECT);
    }

    private void getSerieSelectResult(Intent data) {
        if (data != null) {
            Serie item = data.getParcelableExtra("entity");
            tv_serie.setText(item.getName());
            tv_serie.setTag(item);
            iv_clean_serie.setVisibility(View.VISIBLE);
        }
    }

    private void showEngineSelectActvity() {
        Intent intent = new Intent(this, EngineSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_ENGINE_SELECT);
    }

    private void getEngineSelectResult(Intent data) {
        if (data != null) {
            Engine item = data.getParcelableExtra("entity");
            tv_engine.setText(item.getBrandName() + item.getName());
            tv_engine.setTag(item);
            iv_clean_engine.setVisibility(View.VISIBLE);
        }
    }

    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryIndexActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    private void showEngineCategoryWarning() {
        Category item = (Category) tv_part.getTag();
        if (item == null)
            return;

        if (item.getPid() == APP.CATEGORY_ENGINE_PART) {
            if (ll_engine.getVisibility() == View.GONE) {
                tv_part_tip.setText("发动机件,请选择发动机型号");
            }else{
                tv_part_tip.setText("");
            }
        }
    }

    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            Category item = data.getParcelableExtra("entity");
            tv_part.setText(item.getName());
            tv_part.setTag(item);

            // 发动机件,提示按发动机型号选择
            showEngineCategoryWarning();
            iv_clean_category.setVisibility(View.VISIBLE);
        }
    }

    private void showBrandSelectActvity() {
        Intent intent = new Intent(this, BrandSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_BRAND_SELECT);
    }

    private void getBrandSelectResult(Intent data) {
        if (data != null) {
            Brand item = data.getParcelableExtra("entity");
            tv_brand.setText(item.getName());
            tv_brand.setTag(item);
            iv_clean_brand.setVisibility(View.VISIBLE);
        }
    }

    private void showModelOrEngineSelect() {
        String name = tv_switch.getText().toString();
        if (name.contains("发动机")) {
            tv_switch.setText("按挖机机型");
            ll_serie.setVisibility(View.GONE);
            ll_engine.setVisibility(View.VISIBLE);
        }else{
            tv_switch.setText("按发动机");
            ll_serie.setVisibility(View.VISIBLE);
            ll_engine.setVisibility(View.GONE);
        }
        showEngineCategoryWarning();
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_serie:
                    showSerieSelectActvity();
                    break;
                case R.id.ll_engine:
                    showEngineSelectActvity();
                    break;
                case R.id.ll_part:
                    showCategorySelectActvity();
                    break;
                case R.id.ll_brand:
                    showBrandSelectActvity();
                    break;
                case R.id.tv_switch:
                    showModelOrEngineSelect();
                    break;
                case R.id.tv_advance:
                    showAdanceView();
                    break;
                case R.id.iv_clean_serie:
                    tv_serie.setText("");
                    tv_serie.setTag(null);
                    iv_clean_serie.setVisibility(View.INVISIBLE);
                    refresh();
                    break;
                case R.id.iv_clean_engine:
                    tv_engine.setText("");
                    tv_engine.setTag(null);
                    iv_clean_engine.setVisibility(View.INVISIBLE);
                    refresh();
                    break;
                case R.id.iv_clean_category:
                    tv_part.setText("");
                    tv_part.setTag(null);
                    iv_clean_category.setVisibility(View.INVISIBLE);
                    refresh();
                    break;
                case R.id.iv_clean_brand:
                    tv_brand.setText("");
                    tv_brand.setTag(null);
                    iv_clean_brand.setVisibility(View.INVISIBLE);
                    refresh();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_SERIE_SELECT:
                    getSerieSelectResult(data);
                    break;
                case APP.REQUEST_ENGINE_SELECT:
                    getEngineSelectResult(data);
                    break;
                case APP.REQUEST_CATEGORY_SELECT:
                    getCategorySelectResult(data);
                    break;
                case APP.REQUEST_BRAND_SELECT:
                    getBrandSelectResult(data);
                    break;
            }
            refresh();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}