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
import com.topway.fine.model.Company;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import java.util.List;

/**
 * 公司列表数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CompanyAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private SparseArray<Company> selects;
    private SparseArray<Company> checked;
    private List data;
    private long condition;
    private int pageIndex;
    private boolean isMultiSelect;
    private String search;

    public CompanyAdapter(Context context, LoadMoreListView listView) {
        this.context = context;
        this.listView = listView;
        this.isMultiSelect = false;
        this.condition = APP.COMPANY_COMMON;
        this.search = "";
        selects = new SparseArray<Company>();
        checked = new SparseArray<Company>();
    }

    public void setCondition(long condition) {
        this.condition = condition;
        initData();
    }

    public void setMultiSelect(boolean multiSelect) {
        this.isMultiSelect = multiSelect;
    }

    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    public void search(String keyword) {
        search = keyword;
        refresh();
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
        List result = DatabaseHelper.instance().getCompany(search, pageIndex);
        listView.updateLoadMoreViewText(result);
        return result;
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
            convertView = inflater.inflate(R.layout.list_company_item, null);
            holder = new ViewHolder();
            holder.tv_full_name = (TextView) convertView.findViewById(R.id.tv_full_name);
            holder.tv_short_name = (TextView) convertView.findViewById(R.id.tv_short_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.ck_select = (CheckBox) convertView.findViewById(R.id.ck_select);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final View itemView = convertView;
        holder.ck_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkbox = (CheckBox)v;
                if (checkbox.isChecked()) {
                    Company item = (Company)getItem(position);
                    selects.put(position, item);
                    itemView.setBackgroundResource(R.color.day_layout_bg_normal);
                }else{
                    selects.remove(position);
                    itemView.setBackgroundResource(R.drawable.bg_group_item);
                }
            }
        });

        if (isMultiSelect) {
            holder.ck_select.setVisibility(View.VISIBLE);
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
        TextView tv_full_name;
        TextView tv_short_name;
        TextView tv_address;
        CheckBox ck_select;

        public void setValue(int position) {
            Company item = (Company)getItem(position);
            if (item != null) {
                tv_full_name.setText(item.getName());
                tv_short_name.setText(item.getName());
                tv_full_name.setText(item.getName());
            }
            ck_select.setChecked(isChecked(position));
        }
    }

}
