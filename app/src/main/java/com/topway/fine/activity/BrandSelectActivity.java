package com.topway.fine.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.BrandSelectAdapter;
import com.topway.fine.adapter.CategoryMultiAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.utils.DeviceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 品牌：从品牌列表中选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class BrandSelectActivity extends BaseActivity {

    @Bind(R.id.listFrame) PtrClassicFrameLayout listFrame;
    @Bind(R.id.listView) LoadMoreListView listView;
    @Bind(R.id.iv_add) ImageView plusView;
    @Bind(R.id.tv_group) TextView tv_group;
    @Bind(R.id.tv_checked) TextView checkView;
    @Bind(R.id.tv_search) EditText tv_search;

    private BrandSelectActivity context;
    private BrandSelectAdapter adapter;
    private boolean isLoadAll;
    private boolean isMultiSelect;
    CategoryGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_select);
        setActivityTitle("选择品牌");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;
        isMultiSelect = false;

        // 获取要旋转的分组
        if (getOperation() == ENTITY_SELECT) {
        }

        // 选择确定按钮事件
        if (isMultiSelect) {
            plusView.setVisibility(View.INVISIBLE);
            checkView.setVisibility(View.VISIBLE);
            checkView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //adapter.setCategoryGroup(group);
                    //finishActivityResult(group);
                }
            });
        }

        adapter = new BrandSelectAdapter(this, listView);
        adapter.setMultiSelect(isMultiSelect);
        listView.setAdapter(adapter);

        // header custom begin
        //final StoreHouseHeader header = new StoreHouseHeader(context);
        //header.setPadding(0, DeviceUtil.dp2px(context, 15), 0, 0);
        //header.initWithString("Fine");
        //header.setTextColor(getResources().getColor(R.color.gray));
        //listFrame.setHeaderView(header);
        //listFrame.addPtrUIHandler(header);

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
                    Brand item = (Brand) adapter.getItem(position);
                    finishActivityResult(item);
                }
            }
        });

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
                    adapter.setSearch("");
                }else{
                    adapter.setSearch(tv_search.getText().toString());
                }
            }
        });
    }

    public void initData() {
        adapter.initData();
    }
}