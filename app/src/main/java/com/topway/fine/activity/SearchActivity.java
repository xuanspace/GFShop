package com.topway.fine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.adapter.SearchNonAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.async.SearchHistoryAsync;
import com.topway.fine.async.SearchHotwordAsync;
import com.topway.fine.async.SearchProductAsync;
import com.topway.fine.async.SearchCategoryAsync;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.fragment.BrandFilterFragment;
import com.topway.fine.fragment.CategoryFilterFragment;
import com.topway.fine.fragment.EngineFilterFragment;
import com.topway.fine.fragment.SerieFilterFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Product;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;
import com.topway.fine.ui.tagview.OnTagClickListener;
import com.topway.fine.ui.tagview.OnTagDeleteListener;
import com.topway.fine.ui.tagview.Tag;
import com.topway.fine.ui.tagview.TagView;
import com.topway.fine.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tyrantgit.explosionfield.ExplosionField;

/**
 * 搜索界面
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SearchActivity extends BaseActivity {

    public static final int SEARCH_CATEGORY = 1;
    public static final int SEARCH_BRAND = 2;
    public static final int SEARCH_PRODUCT = 3;

    public static final int SORT_TAG = 1;
    public static final int BRAND_TAG = 2;
    public static final int CATEGORY_TAG = 3;
    public static final int SERIE_TAG = 4;
    public static final int ENGINE_TAG = 5;


    // 搜索输入条
    @Bind(R.id.ev_search_input) EditText ev_search_input;
    @Bind(R.id.iv_clean_icon) ImageView iv_clean_icon;
    @Bind(R.id.tv_search_button) TextView tv_search_button;
    @Bind(R.id.re_container_button) RelativeLayout re_container_button;

    // 搜索标签
    @Bind(R.id.ll_tag_view) LinearLayout ll_tag_view;
    @Bind(R.id.tagview) TagView search_tag_view;

    // 搜索结果
    @Bind(R.id.re_search_result) RelativeLayout re_search_result;
    @Bind(R.id.lv_search_result) ListView lv_search_result;

    // 热门搜索
    @Bind(R.id.search_hot_panel) LinearLayout search_hot_panel;
    @Bind(R.id.search_hot_content) TagFlowLayout search_hot_content;

    // 搜索历史
    @Bind(R.id.re_search_history) RelativeLayout re_search_history;
    @Bind(R.id.search_history_panel) LinearLayout search_history_panel;
    @Bind(R.id.search_history_list) ListView search_history_list;
    @Bind(R.id.search_history_clear) LinearLayout search_history_clear;

    // 搜索过滤
    @Bind(R.id.ll_search_filter) LinearLayout ll_search_filter;
    @Bind(R.id.re_sort_filter) RelativeLayout re_sort_filter;
    @Bind(R.id.re_brand_filter) RelativeLayout re_brand_filter;
    @Bind(R.id.re_serie_filter) RelativeLayout re_serie_filter;
    @Bind(R.id.re_engine_filter) RelativeLayout re_engine_filter;
    @Bind(R.id.re_part_filter) RelativeLayout re_part_filter;

    // 搜索过滤菜单
    @Bind(R.id.drawer_layout) DrawerLayout drawer_layout;
    @Bind(R.id.drawer_content) RelativeLayout drawer_content;

    // 数据适配
    private SearchActivity context ;
    private QuickAdapter<Category> categoryAdapter;
    private QuickAdapter<Product> productAdapter;
    private QuickAdapter<String> historyAdapter;
    private int searchType = SEARCH_CATEGORY;

    // 过滤菜单fragments
    private HashMap<Integer, Fragment> fragments;

    // 过滤对象
    private Brand brandFilter;
    private Category categoryFilter;
    private Serie serieFilter;
    private Engine engineFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        initSearchView();
        initFilterView();
        initTagsView();
        initHistoryView();
        hideInputKeyborad();
    }

    // 获取请求查询的类型
    protected String getSearchTypeParameter() {
        String searchType = null;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            searchType = bundle.getString("search");
        return searchType;
    }

    // 初始化搜索分类及其Adapter
    public void initData() {
        initHistoryAdapter();
        initCategoryAdapter();
        initProductAdapter();
        initSearchType();
    }

    // 根据参数初始搜索
    public void initSearchType() {
        String searchParam = getSearchTypeParameter();
        if (searchParam != null) {
            // 安装品牌搜索，即安装品牌名去搜索产品
            if (searchParam.contains("brand")) {
                Brand brand = getParameter(Brand.class);
                if (brand != null) {
                    searchProduct(brand);
                }
            }
            // 安装分类搜索，即安装分类的名去搜索产品
            else if (searchParam.contains("category")) {
                Category category = getParameter(Category.class);
                if (category != null) {
                    searchCategoryPath(category);
                }
            }
        }
        else{
            // 获取热门搜索词
            setHistoryViewVisible();
            new SearchHotwordAsync(context).execute();
            new SearchHistoryAsync(context).execute();
        }
    }

    /**
     * 初始化搜索过滤控件
     *  1. 过滤条隐藏
     *  2. 各个过滤点击事件
     */
    public void initFilterView() {
        ll_search_filter.setVisibility(View.GONE);
        re_sort_filter.setOnClickListener(new ClickListener());
        re_brand_filter.setOnClickListener(new ClickListener());
        re_serie_filter.setOnClickListener(new ClickListener());
        re_engine_filter.setOnClickListener(new ClickListener());
        re_part_filter.setOnClickListener(new ClickListener());

        fragments = new HashMap<Integer, Fragment>();
        fragments.put(R.id.re_sort_filter, new BrandFilterFragment());
        fragments.put(R.id.re_brand_filter, new BrandFilterFragment());
        fragments.put(R.id.re_serie_filter, new SerieFilterFragment());
        fragments.put(R.id.re_engine_filter, new EngineFilterFragment());
        fragments.put(R.id.re_part_filter, new CategoryFilterFragment());

        // 关闭手势滑动
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 搜索过滤条按钮点击事件
     */
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isDoubleClick())
                return;

            showFilterActvity(v);
            switch (v.getId()) {
                case R.id.re_sort_filter:
                    break;
                case R.id.re_brand_filter:
                    break;
                case R.id.re_serie_filter:
                    break;
                case R.id.re_engine_filter:
                    break;
                case R.id.re_part_filter:
                    break;
            }
        }
    }

    // 显示排序过滤菜单
    public void showFilterActvity(View v) {
        Fragment fragment = fragments.get(v.getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_content, fragment).commit();
        drawer_layout.openDrawer(drawer_content);
    }

    public void hideKeyboard() {
        // 隐藏键盘
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 初始化搜索标签
     */
    public void initTagsView() {
        search_tag_view.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(int position, Tag tag) {

            }
        });

        search_tag_view.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(int position, Tag tag) {
                switch (tag.id) {
                    case BRAND_TAG:
                        brandFilter = null;
                        break;
                    case CATEGORY_TAG:
                        categoryFilter = null;
                        break;
                    case SERIE_TAG:
                        serieFilter = null;
                        break;
                    case ENGINE_TAG:
                        engineFilter = null;
                        break;
                    default:
                }

                if (search_tag_view.getTags().size() <1 ) {
                    ll_tag_view.setVisibility(View.GONE);
                }
                searchProduct();
            }
        });
    }

    // 初始化搜索控件
    public void initSearchView() {
        // 搜索编辑
        ev_search_input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        /*ev_search_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    hideKeyboard();
                    search();
                }
                return false;
            }
        });*/

        ev_search_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    search();
                }
                return false;
            }
        });

        // 清除按钮
        iv_clean_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ev_search_input.setText("");
            }
        });

        // 搜索按钮
        re_container_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isDoubleClick())
                    return;
                hideKeyboard();
                search();
            }
        });

        // 搜索结果列表项点击
        lv_search_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDoubleClick())
                    return;
                if (searchType == SEARCH_CATEGORY) {
                    Category item = (Category) categoryAdapter.getItem(position);
                    searchProduct(item);
                }
                else if (searchType == SEARCH_PRODUCT) {
                    Product item = (Product) productAdapter.getItem(position);
                    showProductDetail(item);
                }
            }
        });

        // 清除搜索历史按钮
        search_history_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search_history_panel.setVisibility(View.INVISIBLE);
            }
        });

    }

    // 初始化搜索历史控件
    public void initHistoryView() {
        // 热门搜索标签项点击
        search_hot_content.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String item = (String)view.getTag();
                searchProduct(item);
                return true;
            }
        });

        // 搜索历史列表项点击
        search_history_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = (String)parent.getAdapter().getItem(position);
                searchProduct(name);
            }
        });
    }

    // 初始化搜索历史
    public void initHotwordAdapter(List<String> items) {
        search_hot_content.setAdapter(new TagAdapter<String>(items) {
            @Override
            public View getView(FlowLayout parent, int position, String item) {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(
                        R.layout.list_item_flowtag, search_hot_content, false);
                tv.setText(item);
                tv.setTag(item);
                return tv;
            }
        });
    }

    // 初始化搜索历史
    public void initHistoryAdapter() {
        historyAdapter = new QuickAdapter<String>(context, R.layout.item_search_history, null) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_name, item);
            }
        };
        search_history_list.setAdapter(historyAdapter);
    }

    // 初始化搜索结果适配
    public void initCategoryAdapter() {
        categoryAdapter = new QuickAdapter<Category>(
                context, R.layout.list_search_category_item, null) {
            @Override
            protected void convert(BaseAdapterHelper helper, Category item) {
                helper.setText(R.id.tv_name, item.getName());
            }
        };
    }

    // 初始化搜索结果适配
    public void initProductAdapter() {
        productAdapter = new QuickAdapter<Product>(context, R.layout.list_search_product_item, null) {
            @Override
            protected void convert(BaseAdapterHelper helper, Product item) {
                helper.setImageUrl2(R.id.tv_product_pic, item.getImage());
                helper.setText(R.id.tv_product_name, item.getName());
                helper.setText(R.id.tv_product_desc, item.getDescription());
                helper.setText(R.id.tv_price, item.getPrice().toString());
            }
        };
    }

    // 热门搜索结果数据
    public void setHotwordSearchResult(List<String> data) {
        if (data != null && data.size() > 0) {
            initHotwordAdapter(data);
        }else{
            search_hot_panel.setVisibility(View.INVISIBLE);
        }
    }

    // 历史搜索结果数据
    public void setHistorySearchResult(List<String> data) {
        if (data != null && data.size() > 0) {
            historyAdapter.setData(data);
        }else{
            search_history_panel.setVisibility(View.INVISIBLE);
            search_history_clear.setVisibility(View.INVISIBLE);
        }
    }

    // 分类搜索结果数据
    public void setCategorySearchResult(List<Category> data) {
        searchType = SEARCH_CATEGORY;
        setResultViewVisible();
        if (data == null || data.size() == 0) {
            SearchNonAdapter nonAdapter = new SearchNonAdapter(context);
            lv_search_result.setAdapter(nonAdapter);
        }else{
            lv_search_result.setAdapter(categoryAdapter);
            categoryAdapter.setData(data);
        }
    }

    // 根据关键字搜索
    public void search() {
        String keyword = ev_search_input.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            if (ll_search_filter.getVisibility() == View.INVISIBLE) {
                // 搜索的是分类
                historyAdapter.add(keyword);
                new SearchCategoryAsync(context).execute(keyword);
                SearchHistoryAsync.saveData(historyAdapter.getData());
            }else{
                // 搜索的是部件名
                searchProduct();
            }
        }
    }

    // 根据关键字搜索产品
    public void searchProduct(String keyword) {
        if (keyword != null) {
            ev_search_input.setText(keyword);
            new SearchProductAsync(context).execute(APP.SEARCH_PRODUCT, keyword);
        }
    }

    /**
     * 根据分类搜索产品
     * @param item  产品分类
     */
    public void searchProduct(Category item) {
        if (item != null) {
            Long id = item.getId();
            String key = item.getName();
            ev_search_input.setText(key);
            new SearchProductAsync(context).execute(APP.SEARCH_CATEGORY, id.toString(), key);
        }
    }

    // 根据品牌搜索产品
    public void searchProduct(Brand item) {
        if (item != null) {
            Long id = item.getId();
            String key = item.getName();
            ev_search_input.setText(key);
            new SearchProductAsync(context).execute(APP.SEARCH_BRAND, id.toString(), key);
        }
    }

    // 根据分类搜索产品
    public void searchCategoryPath(Category category) {
        if (category != null) {
            boolean isSubCategory = false;
            // 是不是最终分类
            String path =  category.getPath().trim();
            if (path.lastIndexOf('.') == path.length()-1) {
                path += category.getId().toString();
            }else {
                isSubCategory = true;
                path = path + "." + category.getId().toString();
            }

            // 子分类包括所以儿子
            String keyword = "";
            if (isSubCategory) {
                new SearchProductAsync(context).execute(APP.SEARCH_CATEGORY_PATH, path);
            }else{
                new SearchProductAsync(context).execute(APP.SEARCH_CATEGORY, category.getId().toString(), keyword);
            }

        }
    }

    // 根据过滤搜索产品
    public void searchProduct() {
        Product filter = new Product();
        if (brandFilter != null)
            filter.setBrandId(brandFilter.getId());
        if (categoryFilter != null)
            filter.setCategoryId(categoryFilter.getId());
        if (serieFilter != null)
            filter.setSerieId(serieFilter.getId());
        if (engineFilter != null)
            filter.setEngineId(engineFilter.getId());

        filter.setName(ev_search_input.getText().toString());
        String josn = JsonUtils.toJson(filter);
        new SearchProductAsync(context).execute(APP.SEARCH_ALL, josn);
    }

    // 产品搜索结果数据
    public void setProductSearchResult(List<Product> data, int page) {
        searchType = SEARCH_PRODUCT;
        setResultViewVisible();
        if (data == null || data.size() == 0) {
            SearchNonAdapter nonAdapter = new SearchNonAdapter(context);
            lv_search_result.setAdapter(nonAdapter);
        }else{
            ll_search_filter.setVisibility(View.VISIBLE);
            lv_search_result.setAdapter(productAdapter);
            if (page <= 1)
                productAdapter.setData(data);
            else
                productAdapter.addAll(data);
        }
    }

    // 显示产品详细信息
    public void showProductDetail(Product item) {
        if (item != null) {
            item.setDefaultValue();
            Intent intent = new Intent(context, ProductGatherActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("operation", ENTITY_SELECT);
            bundle.putParcelable("entity", (Parcelable) item);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    public void setHistoryViewVisible() {
        re_search_history.setVisibility(View.VISIBLE);
        re_search_result.setVisibility(View.INVISIBLE);
    }

    public void setResultViewVisible() {
        hideInputKeyborad();
        re_search_history.setVisibility(View.INVISIBLE);
        re_search_result.setVisibility(View.VISIBLE);
        lv_search_result.setVisibility(View.VISIBLE);
    }

    public void hideInputKeyborad() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 搜索过滤选择事件
     * @param msg 搜索过滤对象
     */
    @Override
    protected void onEvent(Message msg) {
        if (msg.obj != null) {
            Tag tag = null;
            if(msg.obj instanceof Brand) {
                brandFilter = (Brand)msg.obj;
                tag = new Tag(brandFilter.getName());
                tag.id = BRAND_TAG;
                tag.object = brandFilter;
            }
            else if(msg.obj instanceof Category) {
                categoryFilter = (Category)msg.obj;
                tag = new Tag(categoryFilter.getName());
                tag.id = CATEGORY_TAG;
                tag.object = brandFilter;
            }
            else if(msg.obj instanceof Serie) {
                serieFilter = (Serie)msg.obj;
                tag = new Tag(serieFilter.getName());
                tag.id = SERIE_TAG;
                tag.object = brandFilter;
            }
            else if(msg.obj instanceof Engine) {
                engineFilter = (Engine)msg.obj;
                tag = new Tag(engineFilter.getName());
                tag.id = ENGINE_TAG;
                tag.object = engineFilter;
            }

            if (tag != null) {
                tag.isDeletable = true;
                tag.radius = 0f;
                tag.layoutColor = Color.parseColor("#F44336");
                addFilterTag(tag);
            }
        }
    }

    private void addFilterTag(Tag tag) {
        ll_tag_view.setVisibility(View.VISIBLE);
        List<Tag> tags = search_tag_view.getTags();
        for (int i=0;i<tags.size();i++) {
            if (tags.get(i).id == tag.id) {
                search_tag_view.remove(i);
                break;
            }
        }
        search_tag_view.addTag(tag);
        searchProduct();
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer_layout.isDrawerOpen(Gravity.RIGHT)) {
                drawer_layout.closeDrawers();
                return false;
            }
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }
}

