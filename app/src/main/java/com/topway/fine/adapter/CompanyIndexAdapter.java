package com.topway.fine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Company;
import com.topway.fine.utils.PinyinUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 公司索引数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CompanyIndexAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private ListView listView;
    private List<String> indexs;
    private List data;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    PinyinUtil pinyin = PinyinUtil.getInstance();
    private String search;
    private boolean makeAlpha;

    public CompanyIndexAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.search = "";
        this.makeAlpha = false;
    }

    public void search(String keyword) {
        this.search = keyword.trim();
        initData();
    }

    public void refresh() {
        data.clear();
        initData();
    }

    public void saveAlpha() {
        DatabaseHelper helper = DatabaseHelper.instance();
        for (int i = 0; i < data.size(); i++) {
            Company item = (Company) data.get(i);
            helper.saveCompany(item);
        }
    }

    public void initData() {
        data = DatabaseHelper.instance().getCompany(search);
        if (makeAlpha) {
            for (int i = 0; i < data.size(); i++) {
                Company item = (Company) data.get(i);
                item.setAlpha(pinyin.getAlpha(item.getShortname()));
            }
            Collections.sort(data, new PinyinComparator());
        }
        notifyDataSetChanged();
    }

    public void loadData() {

    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (data != null)
            item = data.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //通过该项的位置，获得所在分类组的索引号
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    //根据分类列的索引号获得该序列的首个位置
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }

    //计算item->position,position->item
    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        indexs = new ArrayList<String>();
        indexs.add("start");
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);

        for (int i = 1; i < getCount(); i++) {
            Company item = (Company)getItem(i);
            String letter = item.getAlpha();
            int section = indexs.size() -1;
            if (indexs.get(section) != null) {
                if(!indexs.get(section).equals(letter)) {
                    indexs.add(letter);
                    section++;
                    positionOfSection.put(section, i);
                }
            }
            sectionOfPosition.put(i, section);
        }
        return indexs.toArray(new String[indexs.size()]);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_company_item2, null);
            holder = new ViewHolder();
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_alpha = (TextView) convertView.findViewById(R.id.tv_alpha);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.view_temp = (View) convertView.findViewById(R.id.view_temp);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setValue(position);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_logo;
        TextView tv_alpha;
        TextView tv_name;
        TextView tv_detail;
        View view_temp;

        public void setValue(int position) {
            Company item = (Company) getItem(position);
            tv_name.setText(item.getShortname());
            tv_detail.setText(item.getName());

            // 设置A-Z字母分割条,通过首字母判断
            String header = item.getAlpha();
            if (position == 0 || header != null && !header.equals(((Company)getItem(position - 1)).getAlpha())) {
                // 如果第当前用户标记了A-Z头显示带字母的分隔条
                if ("".equals(header)) {
                    tv_alpha.setVisibility(View.GONE);
                    view_temp.setVisibility(View.VISIBLE);
                } else {
                    tv_alpha.setVisibility(View.VISIBLE);
                    tv_alpha.setText(header);
                    view_temp.setVisibility(View.GONE);
                }
            } else {
                tv_alpha.setVisibility(View.GONE);
                view_temp.setVisibility(View.VISIBLE);
            }

        }
    }

    @SuppressLint("DefaultLocale")
    public class PinyinComparator implements Comparator<Company> {

        @SuppressLint("DefaultLocale")
        @Override
        public int compare(Company o1, Company o2) {
            String py1 = o1.getAlpha();
            String py2 = o2.getAlpha();
            // 判断是否为空""
            if (isEmpty(py1) && isEmpty(py2))
                return 0;
            if (isEmpty(py1))
                return -1;
            if (isEmpty(py2))
                return 1;
            String str1 = "";
            String str2 = "";
            try {
                str1 = (py1.toUpperCase()).substring(0, 1);
                str2 = (py2.toUpperCase()).substring(0, 1);
            } catch (Exception e) {
                System.out.println("某个str为\" \" 空");
            }
            return str1.compareTo(str2);
        }

        private boolean isEmpty(String str) {
            return "".equals(str.trim());
        }
    }
}

