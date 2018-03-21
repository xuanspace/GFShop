package com.topway.fine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.model.Brand;
import com.topway.fine.utils.PinyinUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;


public class BrandFilterAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private List mDataList;
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private LayoutInflater mInflater;
    PinyinUtil pinyin = PinyinUtil.getInstance();

    public BrandFilterAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List list) {
        this.mDataList = list;
        this.buildIndex();
        notifyDataSetChanged();
    }

    public List getData() {
        return this.mDataList;
    }

    public String getItemText(int position) {
        Brand brand = (Brand) mDataList.get(position);
        return brand.getName();
    }

    public char getItemAlpha(int position) {
        Brand brand = (Brand) mDataList.get(position);
        return brand.getAlpha().charAt(0);
    }

    public String getItemAlphaString(int position) {
        Brand brand = (Brand) mDataList.get(position);
        return brand.getAlpha();
    }

    public void buildIndex() {
        for (int i = 0; i < mDataList.size(); i++) {
            Brand item = (Brand) mDataList.get(i);
            item.setAlpha(pinyin.getAlpha(item.getName()));
        }
        Collections.sort(mDataList, new PinyinComparator());
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        char lastFirstChar = getItemAlpha(0);
        sectionIndices.add(0);
        for (int i = 1; i < getCount(); i++) {
            if (getItemAlpha(i) != lastFirstChar) {
                lastFirstChar = getItemAlpha(i);
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = getItemAlphaString(mSectionIndices[i]);
        }
        return letters;
    }

    @Override
    public int getCount() {
        if (mDataList != null)
            return mDataList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        if (mDataList != null)
            item = mDataList.get(position);
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_index_item, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.item_body_tv1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getItemText(position));
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.list_index_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.item_head_tv);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.text.setText(getItemAlphaString(position));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItemAlpha(position);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        mDataList.clear();
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

    @SuppressLint("DefaultLocale")
    public class PinyinComparator implements Comparator<Brand> {

        @SuppressLint("DefaultLocale")
        @Override
        public int compare(Brand o1, Brand o2) {
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
