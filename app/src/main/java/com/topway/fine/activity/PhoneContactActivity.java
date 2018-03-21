package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.CompanyIndexAdapter;
import com.topway.fine.adapter.PhoneContactAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Company;
import com.topway.fine.model.PhoneContact;
import com.topway.fine.model.Product;
import com.topway.fine.widget.Sidebar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手机联系人列表
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhoneContactActivity extends BaseActivity {

    @Bind(R.id.sidebar) Sidebar sidebar;
    @Bind(R.id.listView) ListView listView;

    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;

    @Bind(R.id.iv_add) ImageView plusView;
    @Bind(R.id.tv_checked) TextView checkView;

    private PhoneContactActivity context;
    private PhoneContactAdapter adapter;
    private boolean isLoadAll = false;
    private boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        setActivityTitle("联系人");
        ButterKnife.bind(this);
        initView();
        initData();
    }

    void initView() {
        context = this;

        // 初始化索引列表
        initListView();
        initSearchView();

        // 获取操作类型
        if (getOperation() == ENTITY_SELECT) {
        }
    }

    public void initListView() {
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

        // 列表数据适配
        sidebar.setListView(listView);
        adapter = new PhoneContactAdapter(context, listView);
        listView.setAdapter(adapter);

        // 列表项点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneContact item = adapter.getContactDetail(position);
                //finishActivityResult(item);
                showPhoneContactDeatil(item, position);
            }
        });
    }

    public void initSearchView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_search.clearFocus();

        // 显示搜索列表
        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                re_search.setVisibility(View.VISIBLE);
                //adapter.saveAlpha();
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
        adapter.init();
    }

    public void showPhoneContactDeatil(PhoneContact item, int position) {
        Intent intent = new Intent(this, PhoneContactDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_NOOP);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    public void showAddActivity() {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_ADD);
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
