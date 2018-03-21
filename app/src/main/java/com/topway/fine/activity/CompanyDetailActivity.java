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
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Company;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 公司详细信息
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CompanyDetailActivity extends BaseActivity {

    @Bind(R.id.ll_address) LinearLayout ll_address;
    @Bind(R.id.ll_phone) LinearLayout ll_phone;
    @Bind(R.id.ll_mobile) LinearLayout ll_mobile;

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_shortname) TextView tv_shortname;
    @Bind(R.id.tv_address) TextView tv_address;
    @Bind(R.id.tv_phone) TextView tv_phone;
    @Bind(R.id.tv_mobile) TextView tv_mobile;
    @Bind(R.id.tv_fax) TextView tv_fax;
    @Bind(R.id.tv_man) TextView tv_man;

    private CompanyDetailActivity context;
    private Company detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);
        setActivityTitle("");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        ll_address.setOnClickListener(new ClickListener());
    }

    private void initData() {
        detail = getParameter(Company.class);
        if (detail != null) {
            tv_name.setText(detail.getName());
            tv_shortname.setText(detail.getShortname());
            tv_address.setText(detail.getAddress());
            tv_phone.setText(detail.getTelephone());
            tv_mobile.setText(detail.getMobile());
            tv_fax.setText(detail.getFax());
            tv_man.setText(detail.getMan());
        }
    }

    public void loadData() {

    }

    public void saveData() {

    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_address:
                    break;
                case R.id.ll_phone:
                    break;
                case R.id.ll_mobile:
                    break;
                case R.id.btn_save:
                    break;
            }
        }
    }
}
