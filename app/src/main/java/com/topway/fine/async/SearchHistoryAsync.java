package com.topway.fine.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.topway.fine.activity.SearchActivity;
import com.topway.fine.app.AppConfig;
import com.topway.fine.app.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索历史异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SearchHistoryAsync extends AsyncTask<Long, Void, List<String>> {

    private ProgressDialog progress;
    private SearchActivity context;

    public SearchHistoryAsync(SearchActivity context){
        this.context = context;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    public static List<String> getData() {
        List data = null;
        try {
            String value = AppContext.get(AppConfig.SEARCH_HISTORY, "");
            if (!TextUtils.isEmpty(value))
                data = JSON.parseArray(value, String.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void saveData(List<String> data) {
        try {
            String value = JSON.toJSONString(data);
            AppContext.set(AppConfig.SEARCH_HISTORY, value);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<String> doInBackground(Long... params) {
        return getData();
    }

    protected void onPostExecute(List<String> data) {
        progress.dismiss();
        context.setHistorySearchResult(data);
    }

}
