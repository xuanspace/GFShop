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
import com.topway.fine.model.Serie;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import java.util.List;

/**
 * 挖机列表数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SerieSelectAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private SparseArray<Serie> selects;
    private SparseArray<Serie> checked;
    private List data;
    private long condition;
    private int pageIndex;
    private boolean isMultiSelect;
    private String search;
    private Manufacturer manufacturer;

    public SerieSelectAdapter(Context context, LoadMoreListView listView) {
        this.context = context;
        this.listView = listView;
        this.isMultiSelect = false;
        this.condition = APP.SERIE_COMMON;
        this.search = "";
        selects = new SparseArray<Serie>();
        checked = new SparseArray<Serie>();
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCondition(long condition) {
        this.condition = condition;
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

    public Manufacturer getDefaultManufacturer() {
        long categoryId = APP.CATEGORY_EXCAVATOR;
        List items = DatabaseHelper.instance().getManufacturersBy(categoryId, condition, 0);
        if (items != null && items.size() > 0) {
            manufacturer = (Manufacturer)items.get(0);
        }
        return manufacturer;
    }

    private List getData() {
        List list = null;
        if (manufacturer == null)
            return list;

        long categoryId = manufacturer.getBrandId();
        list = DatabaseHelper.instance().getSeriesBy(categoryId, condition, pageIndex);
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
            convertView = inflater.inflate(R.layout.list_serie_item, null);
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
                    Serie item = (Serie)getItem(position);
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
            Serie item = (Serie)getItem(position);
            if (item != null) {
                name.setText(item.getName());
            }
            checkBox.setChecked(isChecked(position));
        }
    }

}

