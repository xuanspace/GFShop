package com.topway.fine.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.topway.fine.R;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.mime.content.FileBody;

/**
 * 显示和发布图片
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class PhotoPublishAdapter extends BaseAdapter {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_ADD = 1;

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<String> photos;
    private int selectedPosition = -1;
    private boolean shape;

    public PhotoPublishAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addData(ArrayList<String> photos) {
        if (this.photos == null)
            this.photos = photos;
        else
            this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    public void setData(ArrayList<String> photos) {
        if (this.photos != null) {
            this.photos.clear();
            this.photos = photos;
        }
        notifyDataSetChanged();
    }

    public boolean isAddMore(int position) {
        if (photos == null)
            return true;
        if (position == photos.size())
            return true;
        return false;
    }

    public ArrayList<String> getData( ) {
        return photos;
    }

    @Override
    public int getCount() {
        return photos == null ? 1 : photos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return photos == null ? null : photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getItemType(int position) {
         return (photos == null || photos.size() == position) ? TYPE_ADD : TYPE_IMAGE;
    }

    public boolean isShape() {
        return shape;
    }

    public void setShape(boolean shape) {
        this.shape = shape;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null || getItemType(position) == TYPE_ADD) {
            view = inflater.inflate(R.layout.item_photo_publish, parent, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.bind(position);
        return view;
    }

    public class ViewHolder {
        ImageView photo;

        ViewHolder(View view) {
            photo = (ImageView) view.findViewById(R.id.iv_photo);
            view.setTag(this);
        }

        public void setImageView(ImageView view, String url) {
            if (TextUtils.isEmpty(url))
                return;
            if (url.startsWith("www"))
                url = ImageDownloader.Scheme.HTTP.wrap(url);
            else if (url.startsWith("/"))
                url = ImageDownloader.Scheme.FILE.wrap(url);
            ImageLoader.getInstance().displayImage(url, view);
        }

        void bind(int position) {
            if (getItemType(position) == TYPE_ADD) {
                photo.setImageResource(R.drawable.icon_addpic_unfocused);
            }else{
                String path = (String)getItem(position);
                setImageView(photo, path);
            }
        }
    }

}
