package com.topway.fine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.photoview.PictureBrowse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.activity.GalleryBrowseActivity;
import com.topway.fine.activity.PhotoBrowseActivity;
import com.topway.fine.app.AppConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.lzy.okgo.OkGo.getContext;

/**
 * 图片页面适配器
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */
public class GalleryPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> images;

    public GalleryPagerAdapter(Context context) {
        this.context = context;
    }

    public GalleryPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }

    public void setImages(List<String> images) {
        if (this.images == null) {
            this.images = images;
        }else {
            this.images.clear();
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public List<String> getImages() {
        return images;
    }

    public String getUrl(int position) {
        return  (images == null) ? null : images.get(position);
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
        if (!TextUtils.isEmpty(url)) {
            Uri uri = Uri.parse(url);
            view.setTag(url);
            view.setImageURI(uri);
        }
    }

    public View createImageView(int position) {
        ImageView view = new ImageView(context);
        setImageView(view, getUrl(position));
        //item.setImageResource(imageViewIds[position]);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
        view.setLayoutParams(params);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view;
    }

    public View createDraweeView(int position) {
        SimpleDraweeView view = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.view_image, null);
        setImageView(view, getUrl(position));
        return view;
    }

    public void showGalleryBrowse(int position) {
        Intent intent = new Intent(context, GalleryBrowseActivity.class);
        intent.putStringArrayListExtra("images", (ArrayList<String>) images);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public void showPhotoBrowse(int position) {
        PictureBrowse.newBuilder(context, PhotoBrowseActivity.class)
                .setPhotoStringList((ArrayList<String>)images)
                .setCurrentPosition(position)
                .enabledAnimation(false)
                .start();

    }

    @Override
    public int getCount() {
        return  (images == null) ? 0 : images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = createDraweeView(position);
        container.addView(view);

        final int pos = position;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoBrowse(pos);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}