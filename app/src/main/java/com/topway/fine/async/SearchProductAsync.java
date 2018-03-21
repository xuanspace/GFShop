package com.topway.fine.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.text.TextUtils;

import com.topway.fine.activity.SearchActivity;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppConfig;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.Product;
import com.topway.fine.utils.JsonUtils;
import com.topway.fine.utils.StringUtils;

import java.util.List;

/**
 * 产品搜索异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SearchProductAsync extends AsyncTask<String, Void, List<Product>> {

    private String url = AppConfig.HTTP_DOMAIN + "/api/product/search/%s/?page=%d";
    private SearchActivity context;
    private ProgressDialog progress;

    // 搜索结果
    private int page = 0;
    List<Product> data = null;

    // 构造函数
    public SearchProductAsync(SearchActivity activity){
        this.context = activity;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        //progress.setTitle("Loading...");
        //progress.show();
    }

    protected int getPageParameter(String params[], int index) {
        int value = 0;
        try {
            if (params.length > index)
                value = Integer.parseInt(params[index]);
        }catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return value;
    }

    public String getStringParameter(String params[], int index) {
        String value = null;
        if (params.length > index)
            value = params[index];
        return value;
    }

    protected Long getLongParameter(String params[], int index) {
        Long value = null;
        try {
            if (params.length > index)
                value = Long.parseLong(params[index]);
        }catch (NumberFormatException e) {
            //e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据关键字搜索产品
     * @params
     */
    protected List<Product> doInBackground(String... params) {
        String type = APP.SEARCH_PRODUCT;
        String keyword = null;
        String path = null;

        // 搜索的参数
        try {
            type = params[0];
            if (type.equals(APP.SEARCH_BRAND)) {
                Long id = getLongParameter(params, 1);
                keyword = getStringParameter(params, 2);
                page = getPageParameter(params, 3);
                data = DatabaseHelper.instance().getProductsByBrand(id, keyword, page);
            }
            else if (type.equals(APP.SEARCH_CATEGORY)) {
                Long id = getLongParameter(params, 1);
                keyword = getStringParameter(params, 2);
                page = getPageParameter(params, 3);
                data = DatabaseHelper.instance().getProductsByCategory(id, keyword, page);
            }
            else if (type.equals(APP.SEARCH_CATEGORY_PATH)) {
                path = getStringParameter(params, 1);
                page = getPageParameter(params, 2);
                data = DatabaseHelper.instance().getProductsByCategoryPath(path, page);
            }
            else if (type.equals(APP.SEARCH_PRODUCT)) {
                keyword = getStringParameter(params, 1);
                page = getPageParameter(params, 2);
                data = DatabaseHelper.instance().getProductsByKeyword(type, keyword, page);
            }
            if (type.equals(APP.SEARCH_ALL)) {
                Product product = JsonUtils.fromJson(params[1], Product.class);
                data = DatabaseHelper.instance().findProduct(product, page);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    protected void onPostExecute(List<Product> data) {
        //progress.dismiss();
        context.setProductSearchResult(data, page);
    }

}
