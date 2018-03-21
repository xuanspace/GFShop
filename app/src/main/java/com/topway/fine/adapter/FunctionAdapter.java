package com.topway.fine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.topway.fine.R;
import com.topway.fine.ui.BaseViewHolder;

/**
 * @Description: 功能入口页面gridview的Adapter
 * @author linweixuan@gmail.com
 */
public class FunctionAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {
            "分类目录", "挖掘机", "发动机",
            "品牌", "部件", "功能5",
            "公司", "联系人","功能9",
            "添加产品", "联系人","功能9", };

    public int[] imgs = {
            R.drawable.app_home, R.drawable.app_excavator, R.drawable.app_engine,
            R.drawable.app_category, R.drawable.app_part, R.drawable.app_func_6,
            R.drawable.app_func_7, R.drawable.app_func_8, R.drawable.app_func_8,
            R.drawable.app_func_7, R.drawable.app_func_8, R.drawable.app_func_8 };

    public FunctionAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.layout_main_grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);

        tv.setText(img_text[position]);
        return convertView;
    }

}