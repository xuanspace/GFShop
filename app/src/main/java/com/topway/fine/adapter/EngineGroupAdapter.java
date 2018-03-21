package com.topway.fine.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Manufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 发动机型号数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class EngineGroupAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List data;
    private Map groups = new HashMap<String,Group>();
    private ArrayList<Group> indexs;
    private ExpandableListView listView;
    private int groupReosure = R.layout.expend_engine_group;
    private int itemReosure = R.layout.expend_engine_item;

    public EngineGroupAdapter(Context context, ExpandableListView listview)
    {
        this.context = context;
        this.listView = listview;
    }

    public void setGroupReosure(int reosure) {
        this.groupReosure = reosure;
    }

    public void setItemReosure(int reosure) {
        this.itemReosure = reosure;
    }

    /**
     * 展开所有组节点
     */
    public void expandAll() {
        for (int i = 0; i < indexs.size(); i++) {
            listView.expandGroup(i);
        }
    }

    /**
     * 关闭所有组节点
     */
    public void collapseAll() {
        for (int i = 0; i < indexs.size(); i++) {
            listView.collapseGroup(i);
        }
    }

    /**
     * 按关键字搜索分组数据
     * @param keyword 要搜索的字符串
     */
    public void search(String keyword) {
        this.clean();
        data.clear();
        data = DatabaseHelper.instance().getEngines(keyword);
        parseData(data);
        expandAll();
    }

    public void search2(String keyword) {
        clean();
        keyword = keyword.trim();
        if (keyword.isEmpty()) {
            parseData(data);
            return;
        }

        ArrayList<Engine> result = new ArrayList<Engine>();
        for (int i = 0; i < data.size(); i++) {
            Engine item = (Engine) data.get(i);
            if (item.getName().indexOf(keyword) != -1) {
                result.add(item);
            }
        }
        parseData(result);
        expandAll();
    }

    /**
     * 清除所有分组数据
     */
    public void clean() {
        for (int i = 0; i < indexs.size(); i++)
            indexs.clear();
        groups.clear();
        indexs.clear();
    }

    /**
     * 获取数据,进行分组
     */
    public void initData() {
        String keyword = "";
        groups = new HashMap<String,Group>();
        indexs = new ArrayList<Group>();
        data = DatabaseHelper.instance().getEngines(keyword);
        parseData(data);
    }

    /**
     * 将一个列表数据进行分组
     * @param list 列表数据
     */
    public void parseData(List list) {
        int groupPosition = 0;
        if (list == null)
            return;

        for (int i = 0; i < list.size(); i++) {
            Engine item = (Engine)list.get(i);
            String name = item.getBrandName();

            Group group = (Group)groups.get(name);
            if (group == null) {
                group = new Group();
                group.name = name;
                group.index = groupPosition;
                groups.put(name, group);
                indexs.add(groupPosition, group);
                groupPosition++;
            }
            group.add(item);
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty(int groupPosition) {
        return groups.isEmpty();
    }

    /**
     *
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount()
    {
        return groups.size();
    }

    /**
     *
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition)
    {
        Group group = (Group)getGroup(groupPosition);
        return group.getChindCount();
    }

    /**
     *
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition)
    {
        return indexs.get(groupPosition);
    }

    /**
     *
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        Group group = (Group)getGroup(groupPosition);
        return group.getChind(childPosition);
    }

    /**
     *
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    /**
     *
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    /**
     *
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    /**
     *
     * 设置第一层分组视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded 该组是展开状态还是伸缩状态
     * @param convertView 重用已有的视图对象
     * @param parent 返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        GroupHolder groupHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(groupReosure, null);
            groupHolder = new GroupHolder();
            groupHolder.group_name = (TextView)convertView.findViewById(R.id.group_name);
            groupHolder.group_count = (TextView)convertView.findViewById(R.id.group_count);
            groupHolder.group_indicator = (ImageView)convertView.findViewById(R.id.group_indicator);
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (GroupHolder)convertView.getTag();
        }

        if (!isExpanded)
        {
            groupHolder.group_indicator.setImageResource(R.drawable.qb_down);
        }
        else
        {
            groupHolder.group_indicator.setImageResource(R.drawable.qb_right);
        }

        groupHolder.setValues(groupPosition);
        return convertView;
    }

    /**
     *
     * 设置第二层视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild 子元素是否处于组中的最后一个
     * @param convertView 重用已有的视图(View)对象
     * @param parent 返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        ItemHolder itemHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(itemReosure, null);
            itemHolder = new ItemHolder();
            itemHolder.child_icon = (ImageView)convertView.findViewById(R.id.child_icon);
            itemHolder.child_name = (TextView)convertView.findViewById(R.id.child_name);
            itemHolder.child_tip = (TextView)convertView.findViewById(R.id.child_tip);
            convertView.setTag(itemHolder);
        }
        else
        {
            itemHolder = (ItemHolder)convertView.getTag();
        }

        itemHolder.setValues(groupPosition, childPosition);
        return convertView;
    }

    /**
     *
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    class GroupHolder
    {
        public ImageView group_indicator;
        public TextView group_name;
        public TextView group_count;

        public void setValues(int groupPosition) {
            Group item = (Group)getGroup(groupPosition);
            group_name.setText(item.getName());
            group_count.setText(String.format("%d",item.getChindCount()));
        }
    }

    class ItemHolder
    {
        public ImageView child_icon;
        public TextView child_name;
        public TextView child_tip;

        public void setValues(int groupPosition, int childPosition) {
            Engine item = (Engine)getChild(groupPosition, childPosition);
            child_icon.setImageResource(R.drawable.part);
            child_name.setText(item.getName());
            //img.setBackgroundResource(group.getChild(groupPosition,childPosition));
        }
    }

    /**
     * 分组对象（包括分组名,索引值,组下节点）
     */
    class Group {
        public String name;
        public int index ;
        private ArrayList<Engine> items;

        public Group() {
            index = 0;
            name = "";
            items = new ArrayList<Engine>();
        }

        public String getName() {
            return name;
        }

        public int getIndex() {
            return index;
        }

        public int getChindCount() {
            return items.size();
        }

        public Engine getChind(int index) {
            return items.get(index);
        }

        public void add(Engine item) {
            items.add(item);
        }

        public void clean() {
            items.clear();
        }
    }
}

