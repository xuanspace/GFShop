package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Part;
import com.topway.fine.model.Serie;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 部件Activity
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class PartActivity extends BaseActivity {

    @Bind(R.id.re_header) RelativeLayout re_header;
    @Bind(R.id.re_brand) RelativeLayout re_brand;
    @Bind(R.id.re_suppler) RelativeLayout re_suppler;
    @Bind(R.id.re_detail) RelativeLayout re_detail;
    @Bind(R.id.re_picture) RelativeLayout re_picture;
    @Bind(R.id.tv_part_name) TextView tv_part_name;
    @Bind(R.id.iv_part_logo) ImageView iv_part_logo;
    @Bind(R.id.tv_model_name) TextView tv_model_name;

    private PartActivity context ;
    private Category category; // 部件最终分类
    private Part detail;       // 部件实体
    private Engine engine;
    private Serie serie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part);
        this.setActivityTitle(R.string.part_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_header.setOnClickListener(new ClickListener());
        re_brand.setOnClickListener(new ClickListener());
        re_suppler.setOnClickListener(new ClickListener());
        re_detail.setOnClickListener(new ClickListener());
        re_picture.setOnClickListener(new ClickListener());
    }

    public void initData() {
        // 是从部件分类哪来的
        category = getParameter(Category.class);
        if (category != null) {
            tv_part_name.setText(category.getName());
            //setImageUrl(iv_serie_logo, detail.getImage());
            tv_model_name.setText(getOidPath());
        }

        // 是从发动机型号哪来的
        engine =  getParameter("engine", Engine.class);
        if (engine != null) {
            tv_model_name.setText(engine.getBrandName() + engine.getName());
        }

        // 是从挖机机型哪来的
        serie =  getParameter("serie", Serie.class);
        if (serie != null) {
            tv_model_name.setText(serie.getBrandName() + serie.getName());
        }
    }

    public void showProductActivity() {
        Intent intent = new Intent(this, ProductActivity.class);
        Bundle bundle = new Bundle();

        // 添加更多参数
        if (category != null)
            bundle.putParcelable("entity", (Parcelable)category);
        if (engine != null)
            bundle.putParcelable("engine", (Parcelable)engine);
        if (serie != null)
            bundle.putParcelable("serie", (Parcelable)serie);

        // 添加对象路径
        bundle.putInt("operation", ENTITY_NOOP);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_header:
                    break;
                case R.id.re_brand:
                    // 只显示挖机部件分类目录
                    showProductActivity();
                    break;
                case R.id.re_suppler:
                    break;
                case R.id.re_detail:
                    break;
                case R.id.re_picture:
                    break;

            }
        }
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setOidPath("");
            super.onKeyDown(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
