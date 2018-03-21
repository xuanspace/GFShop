package com.topway.fine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import java.util.Comparator;
import java.util.List;

/**
 * 挖机配件启始分类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryPartAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private List data;

    public CategoryPartAdapter(Context context, LoadMoreListView listView) {
        this.context = context;
        this.listView = listView;
    }

    public void initData() {
        data = DatabaseHelper.instance().getCategoryByFather(APP.CATEGORY_PART);
        listView.updateLoadMoreViewText(data);
        notifyDataSetChanged();
    }

    public void loadData() {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_category_item3, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.name);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.checkBox.setVisibility(View.GONE);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
        CheckBox checkBox;

        public void setValue(int position) {
            Category item = (Category) getItem(position);
            tv_name.setText(item.getName());
        }
    }

}
