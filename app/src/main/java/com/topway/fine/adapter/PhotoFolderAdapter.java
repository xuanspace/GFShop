package com.topway.fine.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.model.AlbumFolder;
import com.topway.fine.model.AlbumPhoto;
import com.topway.fine.utils.AlbumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册目录
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class PhotoFolderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<AlbumFolder> albums;
    private int lastSelected = 0;

    public PhotoFolderAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.albums = new ArrayList<>();
    }

    public void setData(List<AlbumFolder> albums) {
        if (this.albums != null)
            this.albums.clear();
        if (albums != null && albums.size() > 0)
            this.albums = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return albums.size() + 1;
    }

    @Override
    public AlbumFolder getItem(int position) {
        if (position == 0)
            return null;
        return albums.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if (position == 0) {
                holder.name.setText("所有图片");
                holder.size.setText(getAllPhotoCount() + "张");
                if (albums.size() > 0) {
                    AlbumFolder folder = albums.get(0);
                    setImageView(holder.cover, folder.getCover().getPath());
                }
            } else {
                holder.bind(position);
            }
            if (lastSelected == position) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    private int getAllPhotoCount() {
        int total = 0;
        if (albums != null && albums.size() > 0) {
            for (AlbumFolder f : albums)
                total += f.getCount();
        }
        return total;
    }

    public void setSelectIndex(int position) {
        if (lastSelected == position)
            return;
        lastSelected = position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
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

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;
        TextView path;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            path = (TextView) view.findViewById(R.id.path);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bind(int position) {
            AlbumFolder data = getItem(position);
            if (data == null) return;

            name.setText(data.getName());
            size.setText(data.getCount() + "张");
            path.setText(data.getPath());
            setImageView(cover, data.getCover().getPath());

            if (lastSelected == position) {
                indicator.setVisibility(View.VISIBLE);
            } else {
                indicator.setVisibility(View.INVISIBLE);
            }
        }
    }

}