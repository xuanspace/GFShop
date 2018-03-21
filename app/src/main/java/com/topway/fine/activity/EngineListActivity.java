package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.ListActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Engine;
import com.topway.fine.model.EngineDao;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发动机型号列表
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class EngineListActivity extends ListActivity<Engine> {

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_sub_name) TextView tv_sub_name;

    private EngineListActivity context ;
    private String prefixOid;
    private Brand brand;
    private long condition = APP.ENGINE_COMMON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_list);
        setActivityTitle(R.string.engine_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        setDataSource(EngineDao.TABLENAME);
        setItemView(R.layout.list_engine_item);
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Engine item) {
        helper.setText(R.id.engine_name, item.getName());
        helper.setImageUrl(R.id.engine_logo, item.getImage());

        // 设置列表项点击事件
        setMenu(helper, item);
    }

    @Override
    public void initData() {
        prefixOid = "0.0.0.0.";

        // 设置列表选择模式
        if (getOperation() == ENTITY_SELECT)
            setSelectMode(true);

        // 品牌页面点击具体的品牌项传递的品牌对象
        brand = getParameter(Brand.class);
        if (brand != null) {
            tv_name.setText(brand.getName() + "-发动机");
            tv_sub_name.setText(brand.getEname());
        }

        // 加载列表数据
        super.initData();
    }


    public long getCurrentBrandId() {
        long brandId = 1;
        if (brand != null)
            brandId = brand.getId();
        return brandId;
    }

    @Override
    public List getData(int page) {
        return DatabaseHelper.instance().getEngineByBrand(getCurrentBrandId(), condition, page);
    }

    @Override
    protected void OnItemClick(Engine item, int position) {
        setOidPath(prefixOid + item.getId());
        showActivity(EngineActivity.class, item);
    }

    @Override
    protected void OnItemAddClick() {
        showActivity(EngineDetailActivity.class);
    }

    @Override
    protected void OnItemEditClick(Engine item, int position) {

    }

    @Override
    protected void OnItemDeleteClick(Engine item, int position) {

    }

    @Override
    protected void OnItemMoveTop(Engine item, int position) {
        //DatabaseHelper.instance().setSerieTop(item);
        super.OnItemMoveTop(item, position);
    }

    @Override
    protected void OnItemSetCommon(Engine item, int position) {
        //DatabaseHelper.instance().setSerieCommon(item);
    }

    @Override
    protected void OnItemChoose(Engine item, int position) {
        Intent intent = new Intent();
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
    }
}
