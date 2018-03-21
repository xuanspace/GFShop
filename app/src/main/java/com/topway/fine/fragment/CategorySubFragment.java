package com.topway.fine.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.topway.fine.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 子分类页面实现
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */

public class CategorySubFragment extends Fragment {

    public static final String TAG = "CategorySubFragment";

    @Bind(R.id.webView) WebView webView;
    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_index, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        initView();
        initEvent();
        initData();
    }

    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
    public void initView() {
        //设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        webView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        //设置本地调用对象及其接口
        webView.addJavascriptInterface(new JavaScriptInterface(context), "subcategory");
        //载入js
        webView.loadUrl("file:///android_asset/engine.html");
    }

    public void initEvent() {
    }

    public void initData() {
        Long categoryId = getArguments().getLong(TAG);
    }

    // JS调用的android对象方法定义
    public class JavaScriptInterface {
        Context activity;

        public JavaScriptInterface(Context contxt) {
            this.activity = contxt;
        }

        @JavascriptInterface
        public void onCategoryClick(String name) {
            Toast.makeText(activity, name, Toast.LENGTH_LONG).show();
        }
    }
}