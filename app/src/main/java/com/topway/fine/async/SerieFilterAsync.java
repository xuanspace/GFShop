package com.topway.fine.async;

import android.content.Context;
import android.os.AsyncTask;

import com.topway.fine.base.BaseFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.utils.StringUtils;

import java.util.List;

public class SerieFilterAsync extends AsyncTask<String, Void, List<Brand>> {
    private Context context;
    private BaseFragment fragment;
    private String type;

    public SerieFilterAsync(BaseFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    protected void onPreExecute() {
    }

    protected List<Brand> doInBackground(String... params) {
        if (params.length > 0)
            type = params[0];
        Long brandId = StringUtils.toLong(params[1]);
        Long condition = StringUtils.toLong(params[2]);
        Integer pageIndex = StringUtils.toInt(params[3]);
        return DatabaseHelper.instance().getSeriesBy(brandId, condition, pageIndex);
    }

    protected void onPostExecute(List<Brand> result) {
        if (StringUtils.isEmpty(type))
            type = this.getClass().getSimpleName();
        fragment.onAsyncData(type, result);
    }

}
