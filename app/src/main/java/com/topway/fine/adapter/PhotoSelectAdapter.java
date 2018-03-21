package com.topway.fine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.activity.PhotoPreviewActivity;
import com.topway.fine.app.APP;
import com.topway.fine.model.AlbumFolder;
import com.topway.fine.model.AlbumPhoto;
import com.topway.fine.utils.AlbumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册图片选择
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhotoSelectAdapter extends BaseAdapter {

    private static final int TOTAL_LIMIT = 5;
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context context;
    private LayoutInflater inflater;
    private int itemSize;
    private AbsListView.LayoutParams itemLayoutParams;
    private OnItemCheckListener listener;

    private List<AlbumPhoto> photos;
    private ArrayList<AlbumPhoto> selectedImages;


    public PhotoSelectAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.photos = new ArrayList<AlbumPhoto>();
        this.selectedImages = new ArrayList<AlbumPhoto>();

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.itemSize = dm.widthPixels / 3;
        this.itemLayoutParams = new AbsListView.LayoutParams(itemSize, itemSize);
    }

    public void setData(List<AlbumPhoto> photos) {
        if (this.photos != null)
            this.photos.clear();
        if (photos != null && photos.size() > 0)
            this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photos == null ? 1 : photos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_CAMERA : TYPE_NORMAL;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int type = getItemViewType(position);
        if (type == TYPE_CAMERA) {
            view = inflater.inflate(R.layout.list_item_camera, parent, false);
            view.setTag(null);
        }
        else if (type == TYPE_NORMAL) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_image, parent, false);
                holder = new ViewHolder(view);
            }
            else{
                holder = (ViewHolder) view.getTag();
                if (holder == null) {
                    view = inflater.inflate(R.layout.list_item_image, parent, false);
                    holder = new ViewHolder(view);
                }
            }
            holder.bind(position);
        }

        //一些图片比较小，不能占满一个，需要调整下
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if (lp.height != itemSize) {
            view.setLayoutParams(itemLayoutParams);
        }

        return view;
    }

    public class ViewHolder {
        ImageView image;
        CheckBox checkbox;
        View mask;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            checkbox = (CheckBox) view.findViewById(R.id.select_checkbox);
            mask = view.findViewById(R.id.mask);
            view.setTag(this);
        }

        public void setImage(String url) {
            if (TextUtils.isEmpty(url))
                return;
            if (url.startsWith("www"))
                url = ImageDownloader.Scheme.HTTP.wrap(url);
            else if (url.startsWith("/"))
                url = ImageDownloader.Scheme.FILE.wrap(url);

            if (itemSize > 0) {
                ImageSize size = new ImageSize(80, 50);
                ImageLoader.getInstance().displayImage(url, image, size);
            }
        }

        public void setChcekBox(final AlbumPhoto photo) {
            if (selectedImages.contains(photo)) {
                checkbox.setChecked(true);
                mask.setVisibility(View.VISIBLE);
            } else {
                checkbox.setChecked(false);
                mask.setVisibility(View.GONE);
            }

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox me = (CheckBox) v;
                    if (selectedImages.contains(photo)) {
                        selectedImages.remove(photo);
                    } else {
                        if (TOTAL_LIMIT == selectedImages.size()) {
                            me.setChecked(false);
                        } else {
                            selectedImages.add(photo);
                        }
                    }

                    int visibilty = me.isChecked() ? View.VISIBLE : View.GONE;
                    mask.setVisibility(visibilty);
                    if (listener != null) {
                        listener.onItemCheck(selectedImages.size(), TOTAL_LIMIT, 0);
                    }
                }
            });
        }

        public void bind(int position) {
            AlbumPhoto photo = (AlbumPhoto)getItem(position);
            setChcekBox(photo);
            setImage(photo.getPath());
        }
    }

    public ArrayList<AlbumPhoto> getSelectedImages() {
        return selectedImages;
    }

    public interface OnItemCheckListener {
        void onItemCheck(int curSelectedCount, int total, int position);
    }

    public void setOnItemCheckListener(OnItemCheckListener listener) {
        this.listener = listener;
    }

}

