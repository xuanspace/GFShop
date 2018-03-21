package com.topway.fine.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * @FileName CategoryListHeaderView
 * @Description 分类列表添头部View
 * @Author linweixuan@gmail.com
 * @Date 2017/4/13
 * @Version V 1.0
 */

public class CategoryListHeaderView extends LinearLayout {

    private Context context;
    private TagFlowLayout flowLayout;
    private RelativeLayout re_next_bar;
    private TextView tv_head_title;
    private TextView tv_next_title;

    public CategoryListHeaderView(Context context) {
        super(context);
        this.context = context;
        View view = LayoutInflater.from(this.context).inflate(R.layout.fragment_category_tags, null);
        addView(view);
        initView(view);
    }

    public void initView(View view) {
        flowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout);
        tv_head_title = (TextView) view.findViewById(R.id.tv_head_title);
        tv_next_title = (TextView) view.findViewById(R.id.tv_next_title);
        re_next_bar = (RelativeLayout ) view.findViewById(R.id.re_next_bar);
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

    public TagFlowLayout getFlowLayout() {
        return flowLayout;
    }
}