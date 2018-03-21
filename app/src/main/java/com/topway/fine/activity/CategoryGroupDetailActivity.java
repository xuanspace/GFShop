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
import com.topway.fine.model.CategoryGroup;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 添加分组： 部件可以归类莫个分组
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryGroupDetailActivity extends BaseActivity {

    @Bind(R.id.ev_name) EditText ev_name;
    @Bind(R.id.ev_category) EditText ev_category;
    @Bind(R.id.re_category) RelativeLayout re_category;
    @Bind(R.id.btn_save) RelativeLayout btn_save;

    private CategoryGroup detail;
    private CategoryGroupDetailActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_group);
        setActivityTitle("分组");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_category.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());
        if (getOperation() == ENTITY_EDIT)
            setActivityTitle("编辑分组");
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_category:
                    showCategorySelectActvity();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    private void initData() {
        if (getOperation() == ENTITY_EDIT) {
            detail = getParameter(CategoryGroup.class);
        }

        if (detail != null) {
            detail.setDefaultValue();
            ev_name.setText(detail.getName());
            ev_category.setText(detail.getCategory().getName());
            ev_category.setTag(detail.getCategoryId());
        }
    }


    public void loadData() {

    }

    public void saveData() {

        DatabaseHelper helper = DatabaseHelper.instance();
        if (detail == null) {
            detail = new CategoryGroup();
            detail.setId(helper.getNextSequnce());
        }

        detail.setName(getStr(ev_name));
        detail.setCategoryId(getID(ev_category));

        // 设置未设置分类的缺省值
        if (detail.getCategoryId() == 0)
            detail.setCategoryId(new Long(0));

        // 先后端保存,获取主键ID,保存主键到本地
        detail.setDefaultValue();
        helper.saveCategoryGroup(detail);
        finishActivityResult(detail);
    }

    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            Category item = data.getParcelableExtra("entity");
            ev_category.setText(item.getName());
            ev_category.setTag(item.getId());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_CATEGORY_SELECT:
                    getCategorySelectResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
