package com.topway.fine.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.topway.fine.fragment.CategoryFragment;
import com.topway.fine.model.ProvinceModel;
import com.topway.fine.xml.CityXmlParser;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 从XML获取全国省市县数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CityAsync extends AsyncTask<Long, Void, List<ProvinceModel>> {

    private Context context;
    private ProgressDialog progress;
    private CategoryFragment fragment;
    private Long categoryType;

    public CityAsync(CategoryFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    /**
     * 解析省市区的XML数据
     */
    protected List<ProvinceModel> getProvinces() {
        List<ProvinceModel> provinces = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("city.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            CityXmlParser handler = new CityXmlParser();
            parser.parse(input, handler);
            input.close();
            provinces = handler.getDataList();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        return provinces;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(context);
        progress.setTitle("Loading...");
        progress.show();
    }

    protected List<ProvinceModel> doInBackground(Long... params) {
        return getProvinces();
    }

    protected void onPostExecute(List<ProvinceModel> data) {
        progress.dismiss();
        if(data != null && data.size() !=0) {
            //fragment.onCategoryData(data);
        }
    }

}
