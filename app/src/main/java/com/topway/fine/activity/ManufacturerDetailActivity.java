package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Manufacturer;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 制造商：品牌再分类
 *      每个工程机械分类下存在同一个品牌，即一个品牌在多个工程机械分类中。
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ManufacturerDetailActivity extends BaseActivity {

    @Bind(R.id.tv_category) TextView tv_category;
    @Bind(R.id.tv_brand) TextView tv_brand;
    @Bind(R.id.tv_hot) TextView tv_hot;
    @Bind(R.id.re_brand) RelativeLayout re_brand;
    @Bind(R.id.btn_save) Button btn_save;

    private ManufacturerDetailActivity context;
    private Manufacturer detail;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer_detail);
        setActivityTitle(R.string.brand_title_edit);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_brand.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());

        if (getOperation() == ENTITY_ADD)
            setActivityTitle(R.string.brand_title_add);
    }

    private void initData() {
        if (getOperation() == ENTITY_ADD) {
            category = getParameter(Category.class);
            if (category != null) {
                tv_category.setText(category.getName());
                tv_category.setTag(category.getId());
            }
        }
        else if (getOperation() == ENTITY_EDIT) {
            detail = getParameter(Manufacturer.class);
            if (detail != null) {
                category = detail.getCategory();
                tv_category.setText(detail.getCategory().getName());
                tv_category.setTag(detail.getCategoryId());
                tv_brand.setText(detail.getBrand().getName());
                tv_brand.setTag(detail.getBrandId());
                tv_hot.setText(detail.getHot().toString());
            }
        }
    }

    public void loadData() {

    }

    public void saveData() {
        DatabaseHelper helper = DatabaseHelper.instance();
        if (detail == null) {
            detail = new Manufacturer();
            detail.setId(helper.getNextSequnce());
        }

        detail.setCategoryId(getID(tv_category));
        detail.setBrandId(getID(tv_brand));
        detail.setName(getStr(tv_brand));
        detail.setPath(category.getPath());
        detail.setCommon(new Long(1));
        detail.setHot(getInt(tv_hot));

        // 先后端保存,获取主键ID,保存主键到本地
        helper.saveManufacturer(detail);
        finishActivityResult(detail);
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
            tv_brand.setTag(item.getId());
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_brand:
                    showBrandSelectActvity();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_BRAND_SELECT:
                    getBrandSelectResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}