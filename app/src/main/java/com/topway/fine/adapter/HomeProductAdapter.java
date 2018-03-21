package com.topway.fine.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.base.BaseViewAdapter;
import com.topway.fine.model.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品列表数据适配
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class HomeProductAdapter extends BaseViewAdapter {
    private List<Product> list;

    public HomeProductAdapter(Context context, List<Product> data) {
        this.mContext = context;
        //list.add(2, null);
        this.list = data;
        if(data.size()%2 != 0) {
            this.list.set(data.size() - 1, null);
        }
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            view = View.inflate(mContext, R.layout.gridview_new_item, null);
            //if(position == 0){
            //    view = View.inflate(context, R.layout.gridview_new_item_first, null);
            //}
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
        TextView tv_name;
        TextView tv_description;
        TextView tv_price;
        SimpleDraweeView iv_picture;
        ImageView iv_status;

        ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            iv_picture = (SimpleDraweeView) view.findViewById(R.id.iv_picture);
            iv_status = (ImageView) view.findViewById(R.id.iv_status);
        }

        void bind(View view, int position) {
            Product data = (Product)getItem(position);
            if (data == null) {
                view.setVisibility(View.INVISIBLE);
                return;
            }

            if (data.getName() !=null)
            tv_name.setText(data.getName());
            if (data.getDescription() != null)
            tv_description.setText(data.getDescription());
            if (data.getPrice() != null)
            tv_price.setText("￥" + data.getPrice().toString());

            //iv_status.setImageResource(data.getStatus());
            //iv_picture.setContentDescription(data.getId());
            if (data.getPath() !=null)
            setImageView(iv_picture, data.getImage());
        }
    }
}








