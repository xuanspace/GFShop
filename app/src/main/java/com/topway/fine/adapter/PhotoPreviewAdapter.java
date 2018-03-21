package com.topway.fine.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.topway.fine.R;
import com.topway.fine.model.AlbumPhoto;
import com.topway.fine.utils.ImageUtil;
import com.topway.fine.widget.TouchImageView;

import java.util.ArrayList;

/**
 * 相片浏览
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhotoPreviewAdapter extends PagerAdapter {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    private ArrayList<View> viewList = new ArrayList<>();
    private ArrayList<String> data;

    public PhotoPreviewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        for (String item : data) {
            viewList.add(buildView(item));
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public void setImageView(TouchImageView view, String url) {
        if (TextUtils.isEmpty(url))
            return;
        if (url.startsWith("www"))
            url = ImageDownloader.Scheme.HTTP.wrap(url);
        else if (url.startsWith("/"))
            url = ImageDownloader.Scheme.FILE.wrap(url);
        ImageLoader.getInstance().displayImage(url, view);
    }

    public View buildView(String path) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_preview, null);
        TouchImageView ivPhoto = (TouchImageView) view.findViewById(R.id.ltp_photo_iv);
        setImageView(ivPhoto, path);
        return view;
    }

}
