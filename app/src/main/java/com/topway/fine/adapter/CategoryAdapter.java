package com.topway.fine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.model.Category;

import java.util.List;

/**
 * 分组分类数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> data;
    public int selected;

    public CategoryAdapter(Context context){
        this.context = context;
        this.data = null;
        this.selected = 0;
    }

    public CategoryAdapter(Context context, List<Category> data){
        this.context = context;
        this.data = data;
        this.selected = 0;
    }

    public void setData(List<Category> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setSelect(int position) {
        this.selected = position;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            view = View.inflate(context, R.layout.list_category_left_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        holder.bind(view, position);
        return view;
    }

    class ViewHolder {
        RelativeLayout re_category;
        TextView tv_name;
        ImageView iv_picture;

        ViewHolder(View view) {
            re_category = (RelativeLayout) view.findViewById(R.id.re_category);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
        }

        public void setImageView(ImageView view, String url) {
            view.setTag(url);
            Picasso.with(context).load(url)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .tag(this)
                    .into(view);
        }

        public void setViewBackgrand(View view, int position) {
            if (selected == position) {
                view.setBackgroundResource(R.drawable.category_select);
            } else {
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        public void setViewData(Category item) {
            tv_name.setText(item.getName());
        }

        void bind(View view, int position) {
            Category item = (Category)getItem(position);
            if (item == null) {
                view.setVisibility(View.INVISIBLE);
                return;
            }
            setViewBackgrand(view, position);
            setViewData(item);
        }
    }

}
