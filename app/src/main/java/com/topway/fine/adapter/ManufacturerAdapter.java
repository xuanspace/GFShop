package com.topway.fine.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import java.util.List;

/**
 * 品牌列表数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ManufacturerAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private SparseArray<Manufacturer> selects;
    private SparseArray<Manufacturer> checked;
    private List data;
    private long condition;
    private int pageIndex;
    private boolean isMultiSelect;
    private String search;
    private long categoryId;

    public ManufacturerAdapter(Context context, LoadMoreListView listView) {
        this.context = context;
        this.listView = listView;
        this.isMultiSelect = false;
        this.condition = APP.BRAND_COMMON;
        this.search = "";
        this.categoryId = APP.CATEGORY_EXCAVATOR;
        selects = new SparseArray<Manufacturer>();
        checked = new SparseArray<Manufacturer>();
    }

    public void setCondition(long condition) {
        this.condition = condition;
    }

    public void setSearch(String condition) {
        this.search = condition.trim();
    }

    public void setMultiSelect(boolean multiSelect) {
        this.isMultiSelect = multiSelect;
    }

    public boolean getIsMultiSelect() {
        return isMultiSelect;
    }

    public void search(String keyword) {
        this.search = keyword.trim();
        if (search.isEmpty() || search.length() <1)
            return;
        initData();
    }

    public void refresh() {
        data.clear();
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
        list = DatabaseHelper.instance().getManufacturersBy(categoryId, condition, pageIndex);
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
            convertView = inflater.inflate(R.layout.list_brand_item2, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
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
                    Manufacturer item = (Manufacturer)getItem(position);
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

        TextView name;
        CheckBox checkBox;
        public void setValue(int position) {
            Manufacturer item = (Manufacturer)getItem(position);
            if (item != null) {
                name.setText(item.getName());
            }
            checkBox.setChecked(isChecked(position));
        }
    }

}

