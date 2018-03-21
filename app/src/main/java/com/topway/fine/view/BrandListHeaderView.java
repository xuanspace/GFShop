package com.topway.fine.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.adapter.HomeBrandAdapter;
import com.topway.fine.async.BrandHotAsync;
import com.topway.fine.model.Brand;

import java.util.List;

/**
 * @FileName BrandListHeaderView
 * @Description 像列表添加头部View
 * @Author linweixuan@gmail.com
 * @Date 2017/4/12.
 * @Version V 1.0
 */

public class BrandListHeaderView extends LinearLayout {

    private Context context;
    private GridView girdView;
    private RelativeLayout re_next_bar;
    private TextView tv_head_title;
    private TextView tv_next_title;

    public BrandListHeaderView(Context context) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(this.context).inflate(R.layout.fragment_brand_grid, null);
        addView(view);
        initView(view);
    }

    public void initView(View view) {
        girdView = (GridView) view.findViewById(R.id.gv_brand);
        tv_head_title = (TextView) view.findViewById(R.id.tv_head_title);
        tv_next_title = (TextView) view.findViewById(R.id.tv_next_title);
        re_next_bar = (RelativeLayout ) view.findViewById(R.id.re_next_bar);

        /*
        girdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand item = (Brand)parent.getAdapter().getItem(position);
            }
        });
        */
    }

    public void setHeadTtile(String name) {
        tv_next_title.setText(name);
    }

    public void setNextTtile(String name) {
        tv_next_title.setText(name);
    }

    public void setNextBarColor() {
        int color = getResources().getColor(R.color.normal_bg);
        re_next_bar.setBackgroundColor(color);
    }

    public GridView getGirdView() {
        return girdView;
    }
}