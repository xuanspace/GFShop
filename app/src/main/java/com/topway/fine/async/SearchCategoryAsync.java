package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.activity.SearchActivity;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.fragment.CategoryFragment;
import com.topway.fine.model.Category;

import java.util.List;

/**
 * 搜索异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class SearchCategoryAsync extends AsyncTask<String, Void, List<Category>> {

    private SearchActivity context;
    private ProgressDialog progress;

    public SearchCategoryAsync(SearchActivity activity){
        this.context = activity;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    protected List<Category> doInBackground(String... params) {
        List<Category> data = DatabaseHelper.instance().getCategoryByKeyword(params[0]);
        return data;
    }

    protected void onPostExecute(List<Category> data) {
        progress.dismiss();
        context.setCategorySearchResult(data);
    }

}
