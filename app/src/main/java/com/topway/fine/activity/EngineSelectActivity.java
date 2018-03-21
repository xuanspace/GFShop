package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.EngineExpendAdapter;
import com.topway.fine.adapter.EngineGroupAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Engine;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发动机型号选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class EngineSelectActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.listView) ExpandableListView listView;

    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;
    @Bind(R.id.tv_show) TextView tv_show;

    private EngineGroupAdapter adapter;
    private EngineSelectActivity context;
    private Brand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine_select);
        ButterKnife.bind(this);
        setTitle("选择");
        initView();
        initData();
    }

    private void initView() {
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_search.clearFocus();
        //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(tv_search.getWindowToken(),0);
        initListView();
        initSearchView();
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

        // 监听每个分组里子控件的点击事件
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Engine item = (Engine)adapter.getChild(groupPosition,childPosition);
                finishActivityResult(item);
                return true;
            }
        });

        adapter = new EngineGroupAdapter(this, listView);
        listView.setAdapter(adapter);
    }

    private void initSearchView() {
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

        tv_show.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.collapseAll();
            }
        });
    }

    private void initData() {
        if (getOperation() == ENTITY_SELECT) {

        }
        adapter.initData();
    }

    public void loadData() {

    }
}