package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.app.APP;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.fragment.BrandFilterFragment;
import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.model.Brand;
import com.topway.fine.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

public class BrandHotAsync extends AsyncTask<String, Void, List<Brand>> {

    private String url = IMAGE_DOMAIN + "/brand/";
    private Context context;
    private BaseFragment fragment;
    private String type = "";

    public BrandHotAsync(BaseFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    protected List<Brand> doInBackground(String... params) {
        if (params.length > 0)
            type = params[0];

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

    protected void onPostExecute(List<Brand> result) {
        if(result != null && result.size() !=0) {
            if (StringUtils.isEmpty(type))
                type = this.getClass().getSimpleName();
            fragment.onAsyncData(type, result);
        }
    }
}
