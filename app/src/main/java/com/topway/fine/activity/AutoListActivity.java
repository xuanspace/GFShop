package com.topway.fine.activity;

import android.os.Bundle;

import com.topway.fine.R;
import com.topway.fine.adapter.TestAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.base.BaseListView;
import com.topway.fine.model.Brand;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AutoListActivity  extends BaseActivity {

    @Bind(R.id.listView) BaseListView listView;

    private AutoListActivity context;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autolist);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        adapter = new TestAdapter(this, listView);
        adapter.setMultiSelect(false);
        listView.setAdapter(adapter);

        listView.setOnLoadMoreListener(new BaseListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
                listView.onLoadMoreComplete();
            }
        });
    }


    public void initData() {
        // from brand activity
        Brand brand = getParameter("brand", Brand.class);
        adapter.setBrand(brand);
        adapter.initData();
    }
}