package com.topway.fine.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.activity.ProductSelectActivity;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppConfig;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Product;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.utils.NetUtil;

import java.io.File;
import java.util.List;

/**
 * 部件价格列表数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductListAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private SparseArray<Product> selects;
    private SparseArray<Product> checked;
    private List data;
    private int pageIndex;
    private boolean isMultiSelect;
    private String search;
    private Product condition;
    private Brand brand;
    private Serie serie;
    private Engine engine;
    private Category category;
    private ProductListAdapter.OnItemClickListener listener;

    public ProductListAdapter(Context context, LoadMoreListView listView) {
        this.context = context;
        this.listView = listView;
        this.isMultiSelect = false;
        this.search = "";
        selects = new SparseArray<Product>();
        checked = new SparseArray<Product>();
        condition = new Product();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public void setCondition(Product condition) {
        this.condition = condition;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.isMultiSelect = multiSelect;
    }

    public boolean getIsMultiSelect() {
        return isMultiSelect;
    }

    private void parseCondtion() {
        condition.setBrandId(null);
        condition.setCategoryId(null);
        condition.setSerieId(null);
        condition.setEngineId(null);

        if (brand != null)
            condition.setBrandId(brand.getId());
        if (category != null)
            condition.setCategoryId(category.getId());
        if (serie != null)
            condition.setSerieId(serie.getId());
        if (engine != null)
            condition.setEngineId(engine.getId());
    }

    public void search(String keyword) {
        condition.setName(keyword);
        refresh();
    }

    public void refresh() {
        data.clear();
        parseCondtion();
        initData();
    }

    public void initData() {
        pageIndex = 0;
        if (data != null)
            data.clear();
        data = getData();
        notifyDataSetChanged();
    }

    public void loadData() {
        pageIndex++;
        List items = getData();
        if (data != null)
            data.addAll(items);
        else
            data = items;
        notifyDataSetChanged();
    }

    private List getData() {
        List list = null;
        list = DatabaseHelper.instance().findProduct(condition, pageIndex);
        listView.updateLoadMoreViewText(list);
        return list;
    }

    private List getData2() {
        List list = null;
        if (brand != null) {
            //list = DatabaseHelper.instance().getProductsByBrand(brand.getId(), fragment_search_bar, pageIndex);
            list = DatabaseHelper.instance().findProduct(condition, pageIndex);
        }
        else{
            // 根据部件的路径查询相应的商品
            ProductSelectActivity activity = (ProductSelectActivity)context;
            list = DatabaseHelper.instance().getProducts(activity.getOidPath(), pageIndex);
        }
        listView.updateLoadMoreViewText(list);
        return list;
    }

    public boolean isChecked(int position) {
        if (selects.get(position) != null)
            return true;
        return false;
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (data != null)
            item = data.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_good_item2, null);
            holder = new ViewHolder();
            RelativeLayout re_parent = (RelativeLayout)convertView.findViewById(R.id.re_parent);
            holder.logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.brand = (TextView) convertView.findViewById(R.id.tv_brand);
            holder.price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final View itemView = convertView;
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkbox = (CheckBox)v;
                if (checkbox.isChecked()) {
                    Product item = (Product)getItem(position);
                    selects.put(position, item);
                    itemView.setBackgroundResource(R.color.day_layout_bg_normal);
                }else{
                    selects.remove(position);
                    itemView.setBackgroundResource(R.drawable.bg_group_item);
                }
            }
        });

        if (isMultiSelect) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        updateBackground(position, convertView);
        holder.setValue(position);
        return convertView;
    }

    public void updateBackground(int position, View view) {
        if (isChecked(position)) {
            view.setBackgroundResource(R.color.day_layout_bg_normal);
        }else{
            view.setBackgroundResource(R.drawable.bg_group_item);
        }
    }

    class ViewHolder {
        ImageView logo;
        TextView name;
        TextView brand;
        TextView price;
        CheckBox checkBox;

        public void setImageUrl(ImageView view, String imageUrl) {
            String url = AppConfig.IMAGE_DOMAIN;
            if (imageUrl != null)
                url = url + imageUrl;

            Picasso.with(context).load(new File(url))
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .tag(context)
                    .into(view);
        }

        public void setValue(int position) {
            Product item = (Product)getItem(position);
            if (item == null) return;
            /*/ 如果从品牌中来，部件就不设置图标
            if (condition.getBrandId() != null) {
                logo.setVisibility(View.GONE);
            }else{
                setImageUrl(logo, item.getBrand().getImage());
            }
            */
            name.setText(item.getName());
            price.setText(item.getPrice().toString());
            checkBox.setChecked(isChecked(position));

            Brand itemBrand = item.getBrand();
            if (itemBrand != null) {
                brand.setText(itemBrand.getName());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int curSelectedCount, int total, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
