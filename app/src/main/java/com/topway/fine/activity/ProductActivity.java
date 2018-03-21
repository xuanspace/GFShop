package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.base.ListActivity2;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Part;
import com.topway.fine.model.Product;
import com.topway.fine.model.ProductDao;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 部件商品：品牌下的系列型号
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductActivity extends ListActivity2<Product> {

    @Bind(R.id.iv_header_logo) ImageView iv_header_logo;
    @Bind(R.id.tv_header_title) TextView tv_header_title;
    @Bind(R.id.tv_header_subname) TextView tv_header_subname;
    @Bind(R.id.iv_addmore) ImageView iv_addmore;
    @Bind(R.id.re_search) RelativeLayout re_search;
    @Bind(R.id.iv_search) ImageView iv_search;
    @Bind(R.id.tv_search) EditText tv_search;


    // 所属的品牌的部件，一个部件对应几个品牌叫商品
    private Category category;
    private Part part;
    private Product good;
    private Brand brand;
    private Engine engine;
    private Serie serie;
    private String search;
    private ProductActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_good);
        setActivityTitle(R.string.product_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        search = "";
        setDataSource(ProductDao.TABLENAME);
        setItemView(R.layout.list_good_item2);

        iv_addmore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemAddClick();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                re_search.setVisibility(View.VISIBLE);
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
                    search = "";
                }else{
                    search = tv_search.getText().toString().trim();
                }
                refresh();
            }
        });
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Product item) {
        final int position = helper.getPosition();
        ImageView logo = (ImageView) helper.getView().findViewById(R.id.iv_logo);
        if (brand != null) {
            logo.setVisibility(View.GONE);
        }
        else{
            Brand itemBrand = item.getBrand();
            if (itemBrand != null) {
                helper.setText(R.id.tv_brand, itemBrand.getName());
                helper.setImageUrl(R.id.iv_logo, itemBrand.getImage());
                logo.setVisibility(View.VISIBLE);
            }
        }

        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_price, item.getPrice().toString());

        // 设置列表项点击事件
        setMenu(helper, item);

    }

    private void setItemImageMargin(BaseAdapterHelper helper) {
        RelativeLayout re_parent = (RelativeLayout)helper.getView().findViewById(R.id.re_parent);
        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) re_parent.getLayoutParams();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, 50);
        params.leftMargin = 2;
        re_parent.setLayoutParams(params);
    }

    @Override
    public void initData() {
        // 品牌页面点击具体的品牌项传递的品牌对象
        brand = getParameter("brand", Brand.class);
        if (brand != null) {
            tv_header_title.setText(brand.getName());
            tv_header_subname.setText(brand.getEname());
            setImageUrl(iv_header_logo, brand.getImage());
        }

        category = getParameter(Category.class);
        if (category != null) {
            tv_header_title.setText(category.getName());
            tv_header_subname.setText(getOidPath());
        }

        engine = getParameter("engine", Engine.class);
        if (engine != null) {
        }

        serie = getParameter("serie",Serie.class);
        if (serie != null) {
        }

        // 加载列表初始数据
        super.initData();
    }

    @Override
    public List getData(int page) {
        List data = null;
        // 从品牌界面来的
        if (brand != null) {
            data = DatabaseHelper.instance().getProductsByBrand(brand.getId(), search, page);
        }
        // 从发动机界面来的
        else if (engine != null) {
            data = DatabaseHelper.instance().getProductsByEngine(engine.getId(), search, page);
        }
        // 从挖机型号界面来的
        else if (serie != null) {
            data = DatabaseHelper.instance().getProductsBySerie(serie.getId(), search, page);
        }
        // 从分类界面来的
        else if (category != null) {
            data = DatabaseHelper.instance().getProductsByCategory(category.getId(), search, page);
        }
        else {
            data = DatabaseHelper.instance().getProducts(getOidPath(), page);
        }
        return data;
    }

    @Override
    public void showAddActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_ADD);
        if (brand != null)
            bundle.putParcelable("brand", (Parcelable)brand);

        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_ADD);
    }

    @Override
    protected void OnItemClick(Product item, int position) {
        showEditActivity(ProductDetailActivity.class, item, position);
    }

    @Override
    protected void OnItemAddClick() {
        showAddActivity(ProductDetailActivity.class);
    }

    @Override
    protected void OnItemEditClick(Product item, int position) {
        showEditActivity(ProductDetailActivity.class, item, position);
    }

    @Override
    protected void OnItemDeleteClick(Product item, int position) {
        DatabaseHelper.instance().deleteProduct(item);
    }

    @Override
    protected void OnItemEditUpdate(Product item, int position) {
        //item.refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        switch (resultCode) {
            case RESULT_OK:
                break;
            default :
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

}

