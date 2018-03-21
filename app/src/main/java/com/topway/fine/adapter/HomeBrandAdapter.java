package com.topway.fine.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.app.AppConfig;
import com.topway.fine.model.Brand;

import java.util.List;

/**
 * 首页的品牌列表
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class HomeBrandAdapter extends BaseAdapter {
    private Context context;
    private List<Brand> list;

    public HomeBrandAdapter(Context context, List<Brand> data) {
        this.context = context;
        this.list = data;
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
            view = View.inflate(context, R.layout.gridview_brand_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }

        holder.bind(view, position);
        return view;
    }

    public class ViewHolder {
        TextView tv_name;
        //ImageView iv_picture;
        SimpleDraweeView iv_picture;

        ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            //iv_picture = (ImageView) view.findViewById(R.id.iv_picture);
            iv_picture = (SimpleDraweeView) view.findViewById(R.id.iv_picture);
        }

        public void setImageView(ImageView view, String url) {
            view.setTag(url);
            Picasso.with(context).load(url)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .tag(this)
                    .into(view);
        }

        public void setImageView(SimpleDraweeView view, String url) {
            Uri uri = Uri.parse(url);
            view.setTag(url);
            view.setImageURI(uri);
        }

        void bind(View view, int position) {
            Brand data = (Brand)getItem(position);
            if (data == null) {
                view.setVisibility(View.INVISIBLE);
                return;
            }

            tv_name.setText(data.getName());
            setImageView(iv_picture, data.getImage());
        }
    }
}
