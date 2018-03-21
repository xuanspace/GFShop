package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.model.Brand;

import java.util.ArrayList;
import java.util.List;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

/**
 * 获取品牌异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class HomeBrandAsync extends AsyncTask<String, Void, List<Brand>> {

    private String url = IMAGE_DOMAIN + "/brand/";
    private HomeFragment fragment;
    private Context context;
    private ProgressDialog progress;

    public HomeBrandAsync(HomeFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    protected List<Brand> doInBackground(String... params) {
        List<Brand> items = new ArrayList<Brand>();
        Brand brand = new Brand();
        brand.setId(new Long(1));
        brand.setName("小松");
        brand.setImage(url + "brand01.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(2));
        brand.setName("卡特");
        brand.setImage(url + "brand02.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(3));
        brand.setName("日立");
        brand.setImage(url + "brand03.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(5));
        brand.setName("神钢");
        brand.setImage(url + "brand04.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(10));
        brand.setName("斗山");
        brand.setImage(url + "brand05.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(7));
        brand.setName("沃尔沃");
        brand.setImage(url + "brand06.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(11));
        brand.setName("现代");
        brand.setImage(url + "brand07.png");
        items.add(brand);

        brand = new Brand();
        brand.setId(new Long(102));
        brand.setName("三一");
        brand.setImage(url + "brand08.png");
        items.add(brand);
        return items;
    }

    protected void onPostExecute(List<Brand> data) {
        progress.dismiss();
        if(data != null && data.size() !=0) {
            fragment.initBrands(data);
        }
    }
}
