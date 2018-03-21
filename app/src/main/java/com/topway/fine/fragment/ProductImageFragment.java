package com.topway.fine.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.model.SearchParam;
import com.topway.fine.model.SearchShop;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.pulltorefresh.PullToRefreshBase;
import com.topway.fine.ui.pulltorefresh.PullToRefreshListView;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 产品图片
 *
 * @author linweixuan@gmail.com
 * @version 1.0
 * @created 2016-3-11
 */

public class ProductImageFragment extends Fragment {

    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initData();
        initView();
    }

    public void initView() {
    }

    public void initData() {
    }

    public void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(context).resumeTag(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(context).pauseTag(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(context).cancelTag(context);
    }
}