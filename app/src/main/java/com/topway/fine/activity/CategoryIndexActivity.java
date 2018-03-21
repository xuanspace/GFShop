package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryGroupAdapter;
import com.topway.fine.adapter.CategoryIndexAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.UserInfo;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;
import com.topway.fine.ui.layout.VerticalDrawerLayout;
import com.topway.fine.widget.Sidebar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 分类索引选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryIndexActivity extends BaseActivity {

    @Bind(R.id.sidebar) Sidebar sidebar;
    @Bind(R.id.listView) ListView listView;

    @Bind(R.id.re_category) RelativeLayout re_category;
    @Bind(R.id.iv_logo) ImageView iv_logo;
    @Bind(R.id.tv_category) TextView tv_category;

    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;

    private CategoryIndexActivity context ;
    private CategoryIndexAdapter adapter;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_index);
        setActivityTitle("选择部件");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;

        re_category.setOnClickListener(new ClickListener());

        // 列表数据适配
        sidebar.setListView(listView);
        adapter = new CategoryIndexAdapter(context);
        listView.setAdapter(adapter);

        // 列表项点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) adapter.getItem(position);
                finishActivityResult(item);
            }
        });

        // 显示搜索列表
        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                re_search.setVisibility(View.VISIBLE);
                //re_header.setVisibility(View.GONE);
            }
        });

        // 输入搜索关键字
        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    adapter.search("");
                }else{
                    adapter.search(tv_search.getText().toString());
                }
            }
        });
    }

    public void initData() {
        long pid = APP.CATEGORY_ENGINE_PART;
        category = DatabaseHelper.instance().getCategory(pid);
        if (category != null) {
            tv_category.setText(category.getName());
            tv_category.setTag(category.getId());
            setImageUrl(iv_logo, category.getImage());
            adapter.setFatherCategoryId(pid);
            adapter.initData();
        }
    }

    public void loadData() {
    }

    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryPartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            category = data.getParcelableExtra("entity");
            if (category != null) {
                tv_category.setText(category.getName());
                tv_category.setTag(category.getId());
                setImageUrl(iv_logo, category.getImage());
                adapter.setFatherCategoryId(category.getId());
                adapter.refresh();
            }
        }
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_category:
                    showCategorySelectActvity();
                    break;
            }
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