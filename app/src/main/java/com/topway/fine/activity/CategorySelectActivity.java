package com.topway.fine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CategoryMultiAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.model.Engine;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;
import com.topway.fine.utils.DeviceUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;


public class CategorySelectActivity extends BaseActivity {

    @Bind(R.id.listFrame) PtrClassicFrameLayout listFrame;
    @Bind(R.id.listView) LoadMoreListView listView;
    @Bind(R.id.iv_add) ImageView plusView;
    @Bind(R.id.tv_group) TextView tv_group;
    @Bind(R.id.tv_checked) TextView checkView;

    private Activity context;
    private CategoryMultiAdapter adapter;
    private boolean isLoadAll;
    private boolean isMultiSelect;
    CategoryGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_multi);
        setActivityTitle("选择部件");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;
        isMultiSelect = true;

        // 获取要旋转的分组
        if (getOperation() == ENTITY_SELECT) {
            group = getParameter(CategoryGroup.class);
            setTitle(group.getName());
        }

        // 选择确定按钮事件
        if (isMultiSelect) {
            plusView.setVisibility(View.INVISIBLE);
            checkView.setVisibility(View.VISIBLE);
            checkView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    adapter.setCategoryGroup(group);
                    finishActivityResult(group);
                }
            });
        }

        adapter = new CategoryMultiAdapter(this, listView, group);
        adapter.setMultiSelect(isMultiSelect);
        listView.setAdapter(adapter);

        // header custom begin
        final StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, DeviceUtil.dp2px(context, 15), 0, 0);
        header.initWithString("Fine");
        header.setTextColor(getResources().getColor(R.color.gray));
        listFrame.setHeaderView(header);
        listFrame.addPtrUIHandler(header);

        // 下拉刷新
        listFrame.setLastUpdateTimeRelateObject(this);
        listFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                adapter.initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        // 加载更多
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
            }
        });

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!isMultiSelect) {
                    Category item = (Category) adapter.getItem(position);
                    //finishActivityResult(item);
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

    public void initData() {
        adapter.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}