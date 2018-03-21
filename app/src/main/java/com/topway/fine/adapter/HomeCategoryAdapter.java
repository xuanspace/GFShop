package com.topway.fine.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.base.BaseListAdapter;
import com.topway.fine.base.BaseViewAdapter;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;

import java.util.List;

/**
 * 首页的部件分类
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */

public class HomeCategoryAdapter extends BaseListAdapter {

    public HomeCategoryAdapter(Context context, List data) {
        super(context, data);
    }

    public int getResource() {
        return R.layout.grid_category_item;
    }

    public int getCount() {
        // 首页只显示三行三列
        if (mDataList.size() > 9)
            return 9;
        return mDataList.size();
    }

    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {
        TextView tv_name;
        SimpleDraweeView iv_picture;

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_picture = (SimpleDraweeView) view.findViewById(R.id.iv_picture);
        }

        public void bind(int position) {
            Category item = (Category)getItem(position);
            tv_name.setText(item.getName());
            setImageView(iv_picture, item.getImage());
        }
    }
}
