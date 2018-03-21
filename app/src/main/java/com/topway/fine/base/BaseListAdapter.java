package com.topway.fine.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.model.Category;
import com.topway.fine.model.Product;

import java.util.List;

/**
 * 基于List的Adapter基础类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public abstract class BaseListAdapter extends BaseViewAdapter {

    protected List mDataList;
    protected int mResource;

    public BaseListAdapter() {
    }

    public BaseListAdapter(Context context) {
        mContext = context;
    }

    public BaseListAdapter(Context context, List data) {
        mContext = context;
        mDataList = data;
        mResource = getResource();
    }

    public int getResource() {
        return mResource;
    }

    public void setData(List list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void addData(List list) {
        if (mDataList == null)
            mDataList = list;
        else
            mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public List getData() {
        return this.mDataList;
    }

    @Override
    public int getCount() {
        if (mDataList != null)
            return mDataList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (mDataList != null)
            item = mDataList.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(mContext, mResource, null);
            holder = getViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.bind(position);
        return view;
    }

    public ViewHolder getViewHolder(View view) {
        return null;
    }

    public abstract class ViewHolder {
        public ViewHolder() {
        }

        public ViewHolder(View view) {
        }

        public void bind(int position) {
        }
    }
}
