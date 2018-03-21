package com.topway.fine.base;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppConfig;

/**
 * 对各种view控件访问进行封装
 * @version 1.0
 * @created 2016-3-1
 */
public abstract class BaseViewAdapter extends BaseAdapter {

    protected Context mContext;

    /**
     * 设置ImageView
     * @param view ImageView
     * @param url 图片路径
     */
    public void setImageView(ImageView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            view.setTag(url);
            Picasso.with(mContext).load(url)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .tag(this)
                    .into(view);
        }
    }

    /**
     * 设置ImageView
     * @param view SimpleDraweeView
     * @param url 图片路径
     */
    public void setImageView(SimpleDraweeView view, String url) {
        if (!TextUtils.isEmpty(url)) {
            url = AppConfig.getImagePath(url);
            Uri uri = Uri.parse(url);
            view.setTag(url);
            view.setImageURI(uri);
        }
    }

}
