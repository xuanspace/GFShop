package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.activity.SearchActivity;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.fragment.CategoryFragment;
import com.topway.fine.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取部件分类异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class SearchHotwordAsync extends AsyncTask<Long, Void, List<String>> {

    private ProgressDialog progress;
    private SearchActivity context;

    public SearchHotwordAsync(SearchActivity context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    protected List<String> doInBackground(Long... params) {
        List<String> data = new ArrayList<String>();
        data.add("发动机件");
        data.add("液压件");
        data.add("活塞环");
        return data;
    }

    protected void onPostExecute(List<String> data) {
        progress.dismiss();
        if(data != null && data.size() !=0) {
            context.setHotwordSearchResult(data);
        }
    }

}
