package com.topway.fine.base;

import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected Object type = null;
    protected Context context;
    protected BaseActivity activity;
    protected BaseFragment fragment;

    public BaseAsyncTask(BaseFragment fragment){
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    public BaseAsyncTask(BaseActivity activity){
        this.context = activity;
        this.activity = activity;
    }

    public BaseAsyncTask<Params, Progress, Result> tag(Object type) {
        this.type = type;
        return this;
    }

    public Object getTag() {
        return type;
    }

    protected void onPostExecute(Result result) {
        if (fragment != null)
            fragment.onAsyncData(type, result);
    }
}
