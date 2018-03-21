package com.topway.fine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.PhotoPublishAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Product;
import com.topway.fine.model.Part;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 部件品牌：编辑和添加
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductDetailActivity extends BaseActivity {

    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_INDEX = "extra_index";

    @Bind(R.id.scrollView) ScrollView scrollView;
    @Bind(R.id.gv_photo) GridView gridView;

    @Bind(R.id.tv_brand) TextView tv_brand;
    @Bind(R.id.tv_engine) TextView tv_engine;
    @Bind(R.id.tv_category) TextView tv_category;
    @Bind(R.id.tv_name) EditText tv_name;
    @Bind(R.id.ev_price) EditText ev_price;
    @Bind(R.id.ev_quntity) EditText ev_quntity;

    @Bind(R.id.tv_tip) TextView tv_tip;
    @Bind(R.id.re_brand) RelativeLayout re_brand;
    @Bind(R.id.re_engine) RelativeLayout re_engine;
    @Bind(R.id.re_category) RelativeLayout re_category;
    @Bind(R.id.btn_save) Button btn_save;

    private Brand brand;
    private Product entity;
    private ProductDetailActivity context;
    private PhotoPublishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        setActivityTitle(R.string.product_title);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        context = this;
        re_brand.setOnClickListener(new ClickListener());
        re_engine.setOnClickListener(new ClickListener());
        re_category.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());

        ev_price.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        ev_quntity.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        adapter = new PhotoPublishAdapter(context);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new GridViewListener());
    }

    public void initEvent() {
        scrollView.smoothScrollTo(0, 0);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void initData() {
        if (getOperation() == ENTITY_ADD) {
            brand = getParameter("brand", Brand.class);
        }

        if (getOperation() == ENTITY_EDIT) {
            entity = getParameter(Product.class);
            setActivityTitle(entity.getName());
        }

        if (entity != null) {
            entity.setDefaultValue();
            tv_name.setText(entity.getName());
            ev_price.setText(entity.getPrice().toString());
            ev_quntity.setText(entity.getQuantity().toString());

            brand = entity.getBrand();
            if (brand != null) {
                tv_brand.setText(brand.getName());
                tv_brand.setTag(brand.getId());
            }

            Category category = entity.getCategory();
            if (category != null) {
                tv_category.setText(category.getName());
                tv_category.setTag(category.getId());
            }

            Engine engine = entity.getEngine();
            if (engine != null) {
                tv_engine.setText(engine.getName());
                tv_engine.setTag(engine.getId());
            }
        }
        else{
            if (brand != null) {
                tv_brand.setText(brand.getName());
                tv_brand.setTag(brand.getId());
            }
        }
    }


    public void loadData() {

    }

    public void saveData() {
        // 检查是否存在
        DatabaseHelper helper = DatabaseHelper.instance();
        String name = tv_name.getText().toString();
        long brandId = getID(tv_brand);
        long categoryId = getID(tv_category);
        long engineId = getID(tv_engine);
        long serieId = 0;
        long partId = 0;

        if (getOperation() == ENTITY_ADD) {
            Product found = helper.findProduct(name,brandId,categoryId,engineId,serieId);
            if (found != null) {
                tv_tip.setText("部件名已经存在!");
                return;
            }
        }

        // 不是修改原有项
        if (entity == null) {
            entity = new Product();
            entity.setId(helper.getNextSequnce());
        }

        // 各个关联ID
        entity.setBrandId(getID(tv_brand));
        entity.setEngineId(getID(tv_engine));
        entity.setCategoryId(getID(tv_category));
        entity.setPartId(new Long(0));
        entity.setSerieId(new Long(0));

        // 产品主要要素
        entity.setName(getStr(tv_name));
        entity.setPath(getOidPath());
        entity.setPrice(getFloat(ev_price));
        entity.setQuantity(getInt(ev_quntity));

        // 先后端保存,获取主键ID,保存主键到本地
        entity.setDefaultValue();
        helper.saveProduct(entity);
        finishResult(entity);
    }

    public void finishResult(Product item) {
        Intent intent = new Intent();
        intent.putExtra("operation", getOperation());
        intent.putExtra("position", getPosition());
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
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
            brand = data.getParcelableExtra("entity");
            tv_brand.setText(brand.getName());
            tv_brand.setTag(brand.getId());
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
            tv_engine.setText(item.getName());
            tv_engine.setTag(item.getId());

            String postfix = tv_category.getText().toString();
            String name = item.getName() + "-" +  postfix;
            tv_name.setText(name);
        }
    }

    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryIndexActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            Category item = data.getParcelableExtra("entity");
            tv_category.setText(item.getName());
            tv_category.setTag(item.getId());

            String prefix = tv_engine.getText().toString();
            String name = prefix + "-" + item.getName();
            tv_name.setText(name);
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_brand:
                    showBrandSelectActvity();
                    break;
                case R.id.re_engine:
                    showEngineSelectActvity();
                    break;
                case R.id.re_category:
                    showCategorySelectActvity();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    private class GridViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.isAddMore(position)) {
                showPhotoSelectActivity();
            }
            else{
                showPhotoPreviewActivity();
            }
        }
    }

    public void showPhotoSelectActivity() {
        Intent intent = new Intent(this, PhotoSelectActivity.class);
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_SELECT);
    }

    public void showPhotoPreviewActivity() {
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_DATA, adapter.getData());
        intent.putExtra(EXTRA_INDEX, adapter.getSelectedPosition());
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_PREVIEW);
    }

    public void getPhotoSelectResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.addData(images);
        }
    }

    public void getPhotoPreviewResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.setData(images);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_BRAND_SELECT:
                    getBrandSelectResult(data);
                    break;
                case APP.REQUEST_ENGINE_SELECT:
                    getEngineSelectResult(data);
                    break;
                case APP.REQUEST_CATEGORY_SELECT:
                    getCategorySelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_SELECT:
                    getPhotoSelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_PREVIEW:
                    getPhotoPreviewResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}