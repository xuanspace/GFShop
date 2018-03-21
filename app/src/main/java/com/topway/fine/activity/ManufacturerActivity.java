package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Stack;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.ListActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.BrandDao;
import com.topway.fine.model.Category;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

/**
 * 生产商：每个Category分类可能有同一个品牌
 *         相当一个Category子分类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ManufacturerActivity extends ListActivity<Manufacturer> {

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_sub_name) TextView tv_sub_name;
    @Bind(R.id.tv_common) TextView tv_common;
    @Bind(R.id.tv_foreign) TextView tv_foreign;
    @Bind(R.id.tv_china) TextView tv_china;
    @Bind(R.id.tv_all) TextView tv_all;
    @Bind(R.id.tv_add_brand) ImageView tv_add_brand;
    @Bind(R.id.iv_logo) ImageView iv_logo;
    @Bind(R.id.tv_show) TextView tv_show;

    private ManufacturerActivity context;
    private Category category;
    private long condition = APP.BRAND_COMMON;
    private boolean isSelectNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturer);
        setActivityTitle(R.string.brand_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        setDataSource(BrandDao.TABLENAME);
        setItemView(R.layout.list_brand_item);
        setCurrentCategory();
        //setSelectMode(true);

        tv_common.setOnClickListener(new ClickListener());
        tv_foreign.setOnClickListener(new ClickListener());
        tv_china.setOnClickListener(new ClickListener());
        tv_all.setOnClickListener(new ClickListener());
        tv_add_brand.setOnClickListener(new ClickListener());
        tv_show.setOnClickListener(new ClickListener());
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Manufacturer item) {
        Brand brand = item.getBrand();
        helper.setText(R.id.brand_name, brand.getName());
        helper.setImageUrl(R.id.brand_logo, brand.getImage());

        // 设置列表项点击事件
        setMenu(helper, item);
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setCurrentCategory() {
        // 获取Activity启动Category
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = getParameter(Category.class);
        }

        if (category.getId() == APP.CATEGORY_EXCAVATOR) {
            tv_name.setText("挖机品牌");
            tv_sub_name.setText("Excavator Brand");
        }
        else if (category.getId() == APP.CATEGORY_ENGINE) {
            tv_name.setText("发动机型号");
            tv_sub_name.setText("Excavator Engine");
            iv_logo.setImageResource(R.drawable.app_engine);
        }
    }

    @Override
    public List getData(int page) {
        return DatabaseHelper.instance().getManufacturersBy(category.getId(),condition, page);
    }

    public void setCondition(long condition) {
        this.condition = condition;
        refresh();
    }

    public void setConditionBotton() {
        tv_common.setBackgroundResource(0);
        tv_foreign.setBackgroundResource(0);
        tv_china.setBackgroundResource(0);
        tv_all.setBackgroundResource(0);
    }

    public void toggleShowAll() {
        if (tv_show.getText().toString().indexOf("全部") != -1){
            tv_show.setText("显示常用");
            condition = APP.BRAND_ALL;
        }else{
            tv_show.setText("显示全部");
            condition = APP.BRAND_COMMON;
        }
        refresh();
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_show:
                    toggleShowAll();
                    refresh();
                    break;
                case R.id.tv_common:
                    setConditionBotton();
                    condition = APP.BRAND_COMMON;
                    tv_common.setBackgroundResource(R.drawable.shape_button);
                    refresh();
                    break;
                case R.id.tv_foreign:
                    setConditionBotton();
                    condition = APP.BRAND_FOREIGN;
                    tv_foreign.setBackgroundResource(R.drawable.shape_button);
                    refresh();
                    break;
                case R.id.tv_china:
                    setConditionBotton();
                    condition = APP.BRAND_CHINA;
                    tv_china.setBackgroundResource(R.drawable.shape_button);
                    refresh();
                    break;
                case R.id.tv_all:
                    setConditionBotton();
                    condition = APP.BRAND_ALL;
                    tv_all.setBackgroundResource(R.drawable.shape_button);
                    refresh();
                    break;
                case R.id.tv_add_brand:
                    showAddActivity(ManufacturerDetailActivity.class, category);
            }
        }
    }


    @Override
    protected void OnItemClick(Manufacturer item, int position) {
        // 不同的子分类
        if (item.getCategoryId() == APP.CATEGORY_EXCAVATOR) {
            showActivity(SerieListActivity.class, item.getBrand());
        }
        else if (item.getCategoryId() == APP.CATEGORY_ENGINE) {
            showActivity(EngineListActivity.class, item.getBrand());
        }
    }

    @Override
    protected void OnItemAddClick() {
        // 顶部标题弹出菜单
        PopMenuWindow menu = new PopMenuWindow(this);
        menu.showPopupWindow(getAddView());
    }

    @Override
    protected void OnItemEditClick(Manufacturer item, int position) {
        showEditActivity(BrandDetailActivity.class, item, position);
    }

    @Override
    protected void OnItemDeleteClick(Manufacturer item, int position) {
        DatabaseHelper.instance().deleteManufacturer(item);
    }

    @Override
    protected void OnItemMoveTop(Manufacturer item, int position) {
        DatabaseHelper.instance().setManufacturerTop(item);
        super.OnItemMoveTop(item, position);
    }

    @Override
    protected void OnItemSetCommon(Manufacturer item, int position) {
        DatabaseHelper.instance().setManufacturerCommon(item);
    }

    @Override
    protected void OnItemChoose(Manufacturer item, int position) {
        Intent intent = new Intent();
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        switch (resultCode) {
            case RESULT_OK:
                break;
            default :
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
