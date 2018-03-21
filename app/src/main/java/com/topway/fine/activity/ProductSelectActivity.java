package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.ProductListAdapter;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Product;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 部件商品价格
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductSelectActivity extends BaseActivity {

    @Bind(R.id.re_header) RelativeLayout re_header;
    @Bind(R.id.iv_header_logo) ImageView iv_header_logo;
    @Bind(R.id.tv_header_title) TextView tv_header_title;
    @Bind(R.id.tv_header_subname) TextView tv_header_subname;

    @Bind(R.id.listView) LoadMoreListView listView;
    @Bind(R.id.iv_add) ImageView plusView;
    @Bind(R.id.tv_group) TextView tv_group;
    @Bind(R.id.tv_checked) TextView checkView;
    @Bind(R.id.iv_addmore) ImageView iv_addmore;

    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;

    private ProductSelectActivity context;
    private ProductListAdapter adapter;
    private boolean isLoadAll;
    private boolean isMultiSelect;
    private Brand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_select);
        setActivityTitle(R.string.product_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;
        isMultiSelect = false;

        // 获取操作类型
        if (getOperation() == ENTITY_SELECT) {
        }

        // 确定按钮事件
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

        // 添加新项到列表
        iv_addmore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemAddClick();
            }
        });

        adapter = new ProductListAdapter(this, listView);
        adapter.setMultiSelect(isMultiSelect);
        listView.setAdapter(adapter);

        // 加载更多数据
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.loadData();
            }
        });

        // 表项点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!isMultiSelect) {
                    Product item = (Product) adapter.getItem(position);
                    OnItemClick(item, position);
                }
            }
        });

        // 显示搜索列表
        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                re_search.setVisibility(View.VISIBLE);
                re_header.setVisibility(View.GONE);
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
        // from brand activity
        brand = getParameter("brand", Brand.class);
        if (brand != null) {
            tv_header_title.setText(brand.getName());
            tv_header_subname.setText(brand.getEname());
            setImageUrl(iv_header_logo, brand.getImage());
            adapter.setBrand(brand);
        }

        adapter.initData();
    }

    public void showAddActivity() {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_ADD);
        if (brand != null)
            bundle.putParcelable("brand", (Parcelable)brand);

        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_ADD);
    }

    public void showEditActivity(Product item, int position) {
        if (item == null) return;
        Intent intent = new Intent(this, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)item);
        bundle.putInt("operation", ENTITY_EDIT);
        bundle.putInt("position", position);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_EDIT);
    }

    protected void OnItemAddClick() {
        showAddActivity();
    }

    protected void OnItemClick(Product item, int position) {
        showEditActivity(item, position);
    }

    protected void OnItemEditClick(Product item, int position) {
        showEditActivity(item, position);
    }

    protected void OnItemDeleteClick(Product item, int position) {
        DatabaseHelper.instance().deleteProduct(item);
    }

    protected void OnItemEditUpdate(Product item, int position) {
        //item.refresh();
    }
}