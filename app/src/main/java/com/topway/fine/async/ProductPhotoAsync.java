package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.activity.ProductGatherActivity;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Photo;

import java.util.ArrayList;
import java.util.List;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

/**
 * 获取产品图片
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductPhotoAsync extends AsyncTask<Long, Void, List<String>> {

    private String url = IMAGE_DOMAIN + "/brand/";
    private ProductGatherActivity activity;
    private Context context;
    private ProgressDialog progress;

    public ProductPhotoAsync(ProductGatherActivity activity){
        this.activity = activity;
    }

    protected void onPreExecute() {
        //progress = new ProgressDialog(context);
        //progress.setTitle("Loading...");
        //progress.show();
    }

    protected List<String> doInBackground(Long... params) {
        Long productId = params[0];
        List<Photo> data = DatabaseHelper.instance().getProductPhotos(productId);
        List<String> items = new ArrayList<String>();
        for (int i=0;i<data.size();i++) {
            items.add(data.get(i).getUrl());
        }
        return items;
    }

    protected void onPostExecute(List<String> data) {
        //progress.dismiss();
        if(data != null && data.size() !=0) {
            activity.initProductImages(data);
        }
    }
}
