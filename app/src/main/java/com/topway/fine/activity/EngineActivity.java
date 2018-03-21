package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
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
import com.topway.fine.model.EngineDao;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 挖机发动机
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class EngineActivity extends BaseActivity {

    @Bind(R.id.re_part) RelativeLayout re_part;
    @Bind(R.id.re_generic) RelativeLayout re_generic;

    @Bind(R.id.iv_brand_logo) ImageView iv_brand_logo;
    @Bind(R.id.iv_icon_right) ImageView iv_icon_right;
    @Bind(R.id.tv_title_name) TextView tv_title_name;
    @Bind(R.id.tv_title_sub) TextView tv_title_sub;

    private EngineActivity context ;
    private Engine engine;
    private String prefixOid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);
        setActivityTitle(R.string.engine_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_part.setOnClickListener(new ClickListener());
        re_generic.setOnClickListener(new ClickListener());
    }

    public void initData() {
        // 发动机型号列表选择传入路径
        prefixOid = getOidPath();

        // 上级页面传入的发动机实体
        engine = getParameter(Engine.class);
        if (engine != null) {
            tv_title_name.setText(engine.getName());
            tv_title_sub.setText(engine.getBrandName() + "发动机");
            //setImageUrl(iv_brand_logo, engine.getBrand().getImage());
        }
    }

    public void showCategoryActivity(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)category);
        bundle.putParcelable("engine", (Parcelable)engine);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_part:
                    // 只显示挖机部件分类目录，路径自动带入
                    Category item = DatabaseHelper.instance().getCategory(APP.CATEGORY_PART);
                    showCategoryActivity(item);
                    break;
                case R.id.re_generic:

                    break;
            }
        }
    }
}
