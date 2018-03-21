package com.topway.fine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.model.Serie;

import java.util.List;

public class SerieFilterAdapter extends BaseAdapter {

    private Context mContext;
    private List mDataList;
    private LayoutInflater mInflater;

    public SerieFilterAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
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

    public String getItemText(int position) {
        Serie item = (Serie) mDataList.get(position);
        return item.getName();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_index_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.item_body_tv1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getItemText(position));
        return convertView;
    }

    class ViewHolder {
        TextView text;
    }

}


