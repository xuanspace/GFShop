package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.ui.UIHelper;

/**
 * 标题栏的弹出菜单
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PopMenuWindow extends PopupWindow {

    private View menu;
    private Activity context;

    @SuppressLint("InflateParams")
    public PopMenuWindow(final Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menu = inflater.inflate(R.layout.context_menu_pop, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(menu);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        initBrandMenu();
    }

    public void initBrandMenu() {
        RelativeLayout re_add =(RelativeLayout) menu.findViewById(R.id.re_add);
        RelativeLayout re_common =(RelativeLayout) menu.findViewById(R.id.re_common);
        RelativeLayout re_foreign =(RelativeLayout) menu.findViewById(R.id.re_foreign);
        RelativeLayout re_china =(RelativeLayout) menu.findViewById(R.id.re_china);
        RelativeLayout re_all =(RelativeLayout) menu.findViewById(R.id.re_all);

        re_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context,AddFriendsOneActivity.class));
                UIHelper.showBrandDetailActivity(context, null);
                PopMenuWindow.this.dismiss();
            }
        });

        re_common.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BrandActivity activity = (BrandActivity)context;
                activity.setCondition(APP.BRAND_COMMON);
                PopMenuWindow.this.dismiss();
            }
        });

        re_foreign.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BrandActivity activity = (BrandActivity)context;
                activity.setCondition(APP.BRAND_FOREIGN);
                PopMenuWindow.this.dismiss();
            }
        });

        re_china.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BrandActivity activity = (BrandActivity)context;
                activity.setCondition(APP.BRAND_CHINA);
                PopMenuWindow.this.dismiss();
            }
        });

        re_all.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                BrandActivity activity = (BrandActivity)context;
                activity.setCondition(APP.BRAND_ALL);
                PopMenuWindow.this.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
