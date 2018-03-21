package com.topway.fine.async;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.topway.fine.fragment.HomeFragment;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

/**
 * 获取首页广告图片
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class HomeAdvertiseAsync extends AsyncTask<String, Void, List<String>> {
    private HomeFragment fragment;

    public HomeAdvertiseAsync(HomeFragment fragment){
        this.fragment = fragment;
    }

    protected List<String> doInBackground(String... params) {
        List<String> images = new ArrayList<String>(Arrays.asList(
                IMAGE_DOMAIN + "/ads/ads1.jpg",
                IMAGE_DOMAIN + "/ads/ads2.jpg",
                IMAGE_DOMAIN + "/ads/ads3.jpg",
                IMAGE_DOMAIN + "/ads/ads4.jpg"));
        return images;
    }

    protected void onPostExecute(List<String> result) {
        if(result!=null && result.size()!=0) {
            fragment.initAdvertise(result);
        }
    }

}