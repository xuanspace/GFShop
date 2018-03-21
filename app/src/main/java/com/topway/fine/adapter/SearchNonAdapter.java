package com.topway.fine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.model.Category;

import java.util.List;

/**
 * 搜索为空的适配器
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class SearchNonAdapter extends BaseAdapter {
    private Context mContext;

    public SearchNonAdapter(Context context) {
        mContext = context;
    }

    public void refresh(List<Category> list) {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.view_empty_search, null);
        return convertView;
    }
}