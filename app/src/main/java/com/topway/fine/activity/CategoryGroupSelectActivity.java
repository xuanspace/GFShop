package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryGroupAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 分类选择选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryGroupSelectActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.listView) ExpandableListView listView;

    private CategoryGroupAdapter adapter;
    private CategoryGroupSelectActivity context;
    private Category entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_select);
        setActivityTitle("部件分类");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        getSupportFragmentManager();
        context = this;
        initListView();
    }

    private void initListView() {
        listView.setGroupIndicator(null);

        // 监听组点击
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id)
            {

                if (adapter.isEmpty(groupPosition))
                {
                    return true;
                }
                return false;
            }
        });

        /* 监听每个分组里子控件的点击事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Engine item = (Engine)adapter.getChild(groupPosition,childPosition);
                finishActivityResult(item);
                return false;
            }
        }); */

        adapter = new CategoryGroupAdapter(this);
        listView.setAdapter(adapter);
    }

    private void initData() {
        if (getOperation() == ENTITY_SELECT) {

        }
    }

    public void loadData() {

    }

    public void showMultiSelectActivity(CategoryGroup item, int position) {
        Intent intent = new Intent(this, CategorySelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)item);
        bundle.putInt("operation", ENTITY_SELECT);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, APP.REQUEST_CATEGORY_MULTISELECT);
    }

    public void resultMultiSelectActivity(Intent data) {
        if (data != null) {
            CategoryGroup item = data.getParcelableExtra("entity");
            int position = data.getIntExtra("position", -1);
            adapter.refrshGroup(position);
            listView.expandGroup(position);
        }
    }

    // 选择分类到分组结果返回
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_CATEGORY_MULTISELECT:
                    resultMultiSelectActivity(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}