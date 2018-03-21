package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryGroupAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;
import com.topway.fine.ui.layout.VerticalDrawerLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 分类选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryPickActivity extends BaseActivity {

    @Bind(R.id.re_category_bar) RelativeLayout re_category_bar;

    private TagFlowLayout flowlayout;
    private VerticalDrawerLayout drawerLayout;
    private CategoryPickActivity context ;

    private ExpandableListView listView;
    private CategoryGroupAdapter adapter;
    private Category entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_pick);
        setActivityTitle("");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        initDrawerView();
        initExpendListView();
    }

    private void initDrawerView() {
        context = this;

        drawerLayout = (VerticalDrawerLayout) findViewById(R.id.vertical);
        re_category_bar.setOnClickListener(new ClickListener());

        View view = drawerLayout.getDrawerView();

        flowlayout = (TagFlowLayout) view.findViewById(R.id.flowlayout);
        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                Category item = (Category)view.getTag();
                //Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
                adapter.setCategoryId(item.getId());
                adapter.refreshData();
                return true;
            }
        });

        List items = DatabaseHelper.instance().getSubCategory(3,0);
        flowlayout.setAdapter(new TagAdapter<Category>(items)
        {
            @Override
            public View getView(FlowLayout parent, int position, Category item)
            {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(
                        R.layout.expend_select_item, flowlayout, false);
                tv.setText(item.getName());
                tv.setTag(item);
                return tv;
            }
        });

    }

    private void initExpendListView() {
        View view = drawerLayout.getContentView();
        listView = (ExpandableListView)view.findViewById(R.id.listView);
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

        adapter = new CategoryGroupAdapter(this);
        listView.setAdapter(adapter);
    }

    public void initData() {
        //DatabaseHelper.instance().getSubCategory(3,0);
    }

    public void onClick(View v) {
        if (drawerLayout.isDrawerOpen()) {
            drawerLayout.closeDrawer();
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_category_bar:
                    drawerLayout.openDrawerView();
                    break;
            }
        }
    }

}
