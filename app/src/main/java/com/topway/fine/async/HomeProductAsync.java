package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.fragment.HomeFragment;
import com.topway.fine.model.Product;
import com.topway.fine.utils.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取产品异步任务
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class HomeProductAsync extends AsyncTask<String, Void, Map<String,Object>> {

    private String url = "http://mw.vmall.com/homeRegionList.json";
    private HomeFragment fragment;
    private Context context;
    private ProgressDialog progress;

    public HomeProductAsync(HomeFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    public static Map<String,Object> parseJson(byte[] buff) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<Product> list = null;
        Product goods = null;

        try {
            JSONObject jsonObject = new JSONObject(new String(buff));
            JSONArray jsonArray = jsonObject.getJSONArray("regionList");
            list = new ArrayList<Product>();

            JSONObject jObject0 = jsonArray.getJSONObject(0);
            JSONArray jArray0 = jObject0.getJSONArray("productList");

            int len = jArray0.length();
            for (int i = 0; i < len; i++) {
                JSONObject jo = jArray0.getJSONObject(i);
                goods = new Product();
                goods.setDescription(jo.getString("prdDescription"));
                goods.setName(jo.getString("prdName"));
                goods.setPath(jo.getString("prdPicUrl"));
                goods.setStatus(jo.getLong("prdStatus"));
                try{
                    goods.setPrice(Float.parseFloat(jo.getString("prdUnitPrice")));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                goods.setId(Long.parseLong(jo.getString("prdId")));
                list.add(goods);
            }

            map.put(jObject0.getString("name"), list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    protected Map<String,Object> doInBackground(String... params) {
        Map<String,Object> map = null;
        if(HttpUtil.isHaveInternet(context)) {
            byte[] buff = HttpUtil.getDataFromHttp(url);
            map = parseJson(buff);
        }
        return map;
    }

    protected void onPostExecute(Map<String,Object> result) {
        progress.dismiss();
        if(result != null && result.size() !=0) {
            List<Product> data = (List<Product>) result.get("明星商品栏目");
            fragment.initNewProduct(data);
        }
    }
}
