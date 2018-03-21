package com.topway.fine.async;

import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.model.Category;

import java.util.List;

/**
 * 获取热点异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class HomeCategoryAsync extends AsyncTask<String, Void, List<Category>> {
    private HomeFragment fragment;
    private Context context;

    public HomeCategoryAsync(HomeFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    protected List<Category> doInBackground(String... params) {
        return DatabaseHelper.instance().getCategoryByFather(APP.CATEGORY_PART);
    }

    protected void onPostExecute(List<Category> result) {
        if(result != null && result.size() !=0) {
            fragment.initCategory(result);
        }
    }

}