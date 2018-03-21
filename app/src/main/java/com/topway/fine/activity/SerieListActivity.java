package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.ListActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Serie;
import com.topway.fine.model.SerieDao;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 系列型号：品牌下的系列型号
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SerieListActivity extends ListActivity<Serie> {

    @Bind(R.id.re_brand_bar) RelativeLayout re_brand_bar;
    @Bind(R.id.iv_brand_logo) ImageView iv_brand_logo;
    @Bind(R.id.tv_add_serie) ImageView tv_add_serie;
    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_sub_name) TextView tv_sub_name;
    @Bind(R.id.tv_show) TextView tv_show;

    private SerieListActivity context;

    // 系列型号所属的品牌
    private Brand brand;
    private long condition = APP.SERIE_COMMON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_list);
        setActivityTitle(R.string.serie_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        setDataSource(SerieDao.TABLENAME);
        setItemView(R.layout.list_series_item);

        re_brand_bar.setOnClickListener(new ClickListener());
        tv_add_serie.setOnClickListener(new ClickListener());
        tv_show.setOnClickListener(new ClickListener());
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Serie item) {
        helper.setText(R.id.serie_name, item.getName());
        helper.setText(R.id.engine_name, item.getEngineName());
        helper.setImageUrl(R.id.serie_logo, item.getImage());

        // 设置列表项点击事件
        setMenu(helper, item);
    }

    @Override
    public void initData() {
        // 品牌页面点击具体的品牌项传递的品牌对象
        brand = getParameter(Brand.class);
        if (brand != null) {
            tv_name.setText(brand.getName());
            tv_sub_name.setText(brand.getEname());
            setImageUrl(tv_add_serie, brand.getImage());
        }
        // 加载列表数据
        super.initData();
    }

    @Override
    public List getData(int page) {
        // 根据当前品牌查询相应的型号系列
        return DatabaseHelper.instance().getSeriesBy(brand.getId(), condition, page);
    }

    public void ShowSeries() {
        if (tv_show.getText().toString().indexOf("显示全部机型") != -1){
            tv_show.setText("显示常用机型");
            condition = APP.SERIE_ALL;
        }else{
            tv_show.setText("显示全部机型");
            condition = APP.SERIE_COMMON;
        }
        refresh();
    }

    public void showBrandEditActivity(Brand item) {
        Intent intent = new Intent(this, BrandDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)item);
        bundle.putInt("operation", ENTITY_EDIT);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, APP.REQUEST_BRAND_EDIT);
    }

    public void getBrandEditResult(Intent data) {
        if (data != null) {
            Brand item = data.getParcelableExtra("entity");
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_brand_bar:
                    showBrandEditActivity(brand);
                    break;
                case R.id.tv_add_serie:
                    showAddActivity(SerieDetailActivity.class);
                    break;
                case R.id.tv_show:
                    ShowSeries();
                    break;
            }
        }
    }

    @Override
    protected void OnItemClick(Serie item, int position) {
        showActivity(SerieActivity.class, item);
    }

    @Override
    protected void OnItemAddClick() {
        // 顶部标题弹出菜单
    }

    @Override
    protected void OnItemEditClick(Serie item, int position) {
        showEditActivity(SerieDetailActivity.class, item, position);
    }

    @Override
    protected void OnItemDeleteClick(Serie item, int position) {
        DatabaseHelper.instance().deleteSerie(item);
    }

    @Override
    protected void OnItemMoveTop(Serie item, int position) {
        DatabaseHelper.instance().setSerieTop(item);
        super.OnItemMoveTop(item, position);
    }

    @Override
    protected void OnItemSetCommon(Serie item, int position) {
        DatabaseHelper.instance().setSerieCommon(item);
    }

    @Override
    protected void OnItemChoose(Serie item, int position) {
        Intent intent = new Intent();
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_BRAND_EDIT:
                    getBrandEditResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
