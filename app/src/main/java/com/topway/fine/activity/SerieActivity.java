package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.Serie;
import com.topway.fine.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 挖机发动机
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class SerieActivity extends BaseActivity {

    @Bind(R.id.re_serie_bar) RelativeLayout re_serie_bar;
    @Bind(R.id.re_part) RelativeLayout re_part;
    @Bind(R.id.re_agent) RelativeLayout re_agent;
    @Bind(R.id.re_detail) RelativeLayout re_detail;
    @Bind(R.id.re_picture) RelativeLayout re_picture;
    @Bind(R.id.tv_serie_name) TextView tv_serie_name;
    @Bind(R.id.tv_sub_name) TextView tv_sub_name;
    @Bind(R.id.iv_serie_logo) ImageView iv_serie_logo;

    private SerieActivity context ;
    private Serie detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        setActivityTitle(R.string.serie_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_serie_bar.setOnClickListener(new ClickListener());
        re_part.setOnClickListener(new ClickListener());
        re_agent.setOnClickListener(new ClickListener());
        re_detail.setOnClickListener(new ClickListener());
        re_picture.setOnClickListener(new ClickListener());
    }

    public void initData() {
        setCurrentDetail();
    }

    public void setCurrentDetail() {
        detail = getParameter(Serie.class);
        if (detail != null) {
            tv_serie_name.setText(detail.getBrand().getName() + detail.getName());
            if (StringUtils.isEmpty(detail.getEngineName()))
                tv_sub_name.setText(detail.getBrand().getEname());
            else
                tv_sub_name.setText(detail.getEngineName());
            setImageUrl(iv_serie_logo, detail.getImage());

            // 传递实体OID路径
            setOidPath(detail.getPath() + "." + detail.getId().toString());
        }
    }

    public void showEditActivity() {
        Intent intent = new Intent(this, SerieDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", detail);
        bundle.putInt("operation", ENTITY_EDIT);
        //bundle.putInt("position", position);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_EDIT);
    }

    public void showCategoryActivity() {
        // 设置挖机机型缺省打开分类类型
        Category category = DatabaseHelper.instance().getCategory(APP.CATEGORY_PART);
        Intent intent = new Intent(this, CategoryActivity.class);
        Bundle bundle = new Bundle();

        // 添加更多参数
        if (category != null)
            bundle.putParcelable("entity", (Parcelable)category);
        if (detail != null)
            bundle.putParcelable("serie", (Parcelable)detail);

        // 添加对象路径
        bundle.putInt("operation", ENTITY_NOOP);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    public void showPartsActivity() {
        // 挖机系列型号->挖机部件分类目录->部件
        Category item = DatabaseHelper.instance().getCategory(APP.CATEGORY_PART);

        // 当前的挖机型号的OID路径会带到部件中
        showActivity(CategoryActivity.class, item);
    }

    private void getEditResult(Intent data) {
        if (data != null) {
            Serie item = data.getParcelableExtra("entity");
            tv_serie_name.setText(item.getBrand().getName() + item.getName());

            if (StringUtils.isEmpty(item.getEngineName()))
                tv_sub_name.setText(item.getBrand().getEname());
            else
                tv_sub_name.setText(item.getEngineName());

            detail = item;
        }
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_serie_bar:
                    showEditActivity();
                    break;
                case R.id.re_part:
                    showCategoryActivity();
                    break;
                case R.id.re_agent:
                    break;
                case R.id.re_detail:
                    break;
                case R.id.re_picture:
                    break;

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ENTITY_EDIT:
                    getEditResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
