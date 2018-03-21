package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.ListActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.BrandDao;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 品牌：各个品牌列表,可根据Category分类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class BrandActivity extends ListActivity<Brand> {

    @Bind(R.id.tv_show) TextView tv_show;
    @Bind(R.id.tv_common) TextView tv_common;
    @Bind(R.id.tv_foreign) TextView tv_foreign;
    @Bind(R.id.tv_china) TextView tv_china;
    @Bind(R.id.tv_all) TextView tv_all;
    @Bind(R.id.tv_add_brand) ImageView tv_add_brand;
    @Bind(R.id.re_brand_bar) RelativeLayout re_brand_bar;

    private BrandActivity context;
    private long category = APP.CATEGORY_UNKNOW;
    private long condition = APP.BRAND_COMMON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        setActivityTitle(R.string.brand_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        setDataSource(BrandDao.TABLENAME);
        setItemView(R.layout.list_brand_item);

        tv_show.setOnClickListener(new ClickListener());
        tv_common.setOnClickListener(new ClickListener());
        tv_foreign.setOnClickListener(new ClickListener());
        tv_china.setOnClickListener(new ClickListener());
        tv_all.setOnClickListener(new ClickListener());
        tv_add_brand.setOnClickListener(new ClickListener());
        re_brand_bar.setOnClickListener(new ClickListener());
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Brand item) {
        helper.setText(R.id.brand_name, item.getName());
        helper.setImageUrl(R.id.brand_logo, item.getImage());

        // 设置列表项点击事件
        setMenu(helper, item);
    }

    @Override
    public void initData() {
        // 设置列表选择模式
        if (getOperation() == ENTITY_SELECT)
            setSelectMode(true);

        super.initData();
    }

    @Override
    public List getData(int page) {
        return DatabaseHelper.instance().getBrandsBy(condition, page);
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
                    showAddActivity(BrandDetailActivity.class);
                    break;
                case R.id.re_brand_bar:
                    //showEditActivity(BrandDetailActivity.class,);
                    break;
            }
        }
    }

    public void showPartActivity(Brand item) {
        Intent intent = new Intent(this, ProductActivity.class);
        //Intent intent = new Intent(this, ProductSelectActivity.class);
        //Intent intent = new Intent(this, AutoListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("brand", (Parcelable)item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void OnItemClick(Brand item, int position) {

        if (item.getCategoryId() == APP.CATEGORY_MACHINE) {
            showActivity(SerieListActivity.class, item);
        }else{
            showPartActivity(item);
        }
    }

    @Override
    protected void OnItemAddClick() {
        // 顶部标题弹出菜单
        PopMenuWindow menu = new PopMenuWindow(this);
        menu.showPopupWindow(getAddView());
    }

    @Override
    protected void OnItemEditClick(Brand item, int position) {
        showEditActivity(BrandDetailActivity.class, item, position);
    }

    @Override
    protected void OnItemDeleteClick(Brand item, int position) {
        DatabaseHelper.instance().deleteBrand(item);
    }

    @Override
    protected void OnItemMoveTop(Brand item, int position) {
        DatabaseHelper.instance().setBrandTop(item);
        super.OnItemMoveTop(item, position);
    }

    @Override
    protected void OnItemSetCommon(Brand item, int position) {
        DatabaseHelper.instance().setBrandCommon(item);
    }

    @Override
    protected void OnItemChoose(Brand item, int position) {
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
