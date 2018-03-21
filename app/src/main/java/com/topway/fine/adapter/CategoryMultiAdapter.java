package com.topway.fine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.ui.loadmore.LoadMoreListView;

import java.util.HashMap;
import java.util.List;

public class CategoryMultiAdapter extends BaseAdapter {

    private Context context;
    private LoadMoreListView listView;
    private SparseArray<Category> selects;
    private SparseArray<Category> checked;
    private List data;
    private int pageIndex;
    private CategoryGroup group;
    private boolean isMultiSelect;


    public CategoryMultiAdapter(Context context, LoadMoreListView listView, CategoryGroup group) {
        this.context = context;
        this.listView = listView;
        this.group = group;
        this.isMultiSelect = true;
        selects = new SparseArray<Category>();
        checked = new SparseArray<Category>();
    }

    public void setMultiSelect(boolean multiSelect) {
        this.isMultiSelect = multiSelect;
    }

    public boolean getIsMultiSelect() {
        return isMultiSelect;
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
        String enginePath = "1.3.200%";
        List list = DatabaseHelper.instance().getCategoryByPath(enginePath, pageIndex);
        int count = getCount();
        for (int i=0; i<list.size(); i++) {
            Category item = (Category)list.get(i);
            if (item.getGid() == group.getId()) {
                selects.put(count+i,item);
                checked.put(item.getId().intValue(), item);
            }
        }

        listView.updateLoadMoreViewText(list);
        return list;
    }

    public boolean isChecked(int position) {
        if (selects.get(position) != null)
            return true;
        return false;
    }

    public void setCategoryGroup(CategoryGroup group) {
        for(int i = 0; i < selects.size(); i++) {
            Category category = selects.valueAt(i);
            if (checked.get(category.getId().intValue()) == null){
                category.setGid(group.getId());
                DatabaseHelper.instance().saveCategory(category);
            }else{
                checked.remove(category.getId().intValue());
            }
        }

        for(int i = 0; i < checked.size(); i++) {
            Category category = checked.valueAt(i);
            category.setGid(new Long(0));
            DatabaseHelper.instance().saveCategory(category);
        }
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
            convertView = inflater.inflate(R.layout.list_category_item3, null);
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
                    Category item = (Category)getItem(position);
                    selects.put(position, item);
                    itemView.setBackgroundResource(R.color.day_layout_bg_normal);
                }else{
                    selects.remove(position);
                    itemView.setBackgroundResource(R.drawable.bg_group_item);
                }
            }
        });

        if (!isMultiSelect) {
            holder.checkBox.setVisibility(View.INVISIBLE);

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
            Category item = (Category)getItem(position);
            if (item != null) {
                name.setText(item.getName());
            }
            checkBox.setChecked(isChecked(position));
        }
    }

}