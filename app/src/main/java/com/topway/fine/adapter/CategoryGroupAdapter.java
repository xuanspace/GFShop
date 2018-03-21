package com.topway.fine.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topway.fine.R;
import com.topway.fine.activity.CategoryGroupSelectActivity;
import com.topway.fine.activity.CategorySelectActivity;
import com.topway.fine.app.APP;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryGroup;
import com.topway.fine.ui.flowlayout.FlowLayout;
import com.topway.fine.ui.flowlayout.TagAdapter;
import com.topway.fine.ui.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 分组分类数据
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryGroupAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Group group;
    private long categoryId;
    private boolean isShowPlus;

    public CategoryGroupAdapter(Activity context)
    {
        this.context = context;
        this.categoryId = APP.CATEGORY_ENGINE_PART;
        isShowPlus = false;
        initData();
    }

    public void setShowPlus(boolean showPlus) {
        isShowPlus = showPlus;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void initData() {
        group = new Group();
        group.loadData();
    }

    public void refreshData() {
        group.clean();
        group.loadData();
        notifyDataSetChanged();
    }

    public void refrshGroup(int postion) {
        group.refreshChild(postion);
        notifyDataSetChanged();
    }

    public boolean isEmpty(int groupPosition) {
        return (group.get(groupPosition) == null);
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
        return group.getGroupCount();
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
        return group.getChildrenCount(groupPosition);
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
        return group.get(groupPosition);
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
        return group.getChild(groupPosition,childPosition);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.expend_category_group, null);
            groupHolder = new GroupHolder();
            groupHolder.group_name = (TextView)convertView.findViewById(R.id.group_name);
            groupHolder.group_count = (TextView)convertView.findViewById(R.id.group_count);
            groupHolder.group_indicator = (ImageView)convertView.findViewById(R.id.group_indicator);
            LinearLayout group_add = (LinearLayout)convertView.findViewById(R.id.group_add);

            if (isShowPlus) {
                final int position = groupPosition;
                group_add.setVisibility(View.VISIBLE);
                group_add.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CategoryGroup item = group.get(position);
                        showMultiSelectActivity(item, position);
                    }
                });
            }
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (GroupHolder)convertView.getTag();
        }

        if (isExpanded)
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
            itemHolder = new ItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.expend_group, null);
            itemHolder.flowlayout = (TagFlowLayout)convertView.findViewById(R.id.flowlayout);
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
        public ImageView group_add;

        public void setValues(int groupPosition) {
            group_name.setText(group.get(groupPosition).getName() + "(" + group.getChildCount(groupPosition) +")");
            group_count.setText(String.format("%d",group.getChildCount(groupPosition)));
        }
    }

    public void showMultiSelectActivity(CategoryGroup item, int position) {
        Intent intent = new Intent(context, CategorySelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)item);
        bundle.putInt("operation", 5);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        context.startActivityForResult(intent, APP.REQUEST_CATEGORY_MULTISELECT);
    }

    public void finishActivityResult(Parcelable item) {
        Intent intent = new Intent();
        intent.putExtra("entity", item);
        context.setResult(-1, intent);
        context.finish();
    }

    class ItemHolder
    {
        TagFlowLayout flowlayout;

        public void setValues(int groupPosition, int childPosition) {
            List items = group.getChild(groupPosition);

            flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
            {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent)
                {
                    Category item = (Category)view.getTag();
                    //Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
                    finishActivityResult(item);
                    return true;
                }
            });

            flowlayout.setAdapter(new TagAdapter<Category>(items)
            {
                @Override
                public View getView(FlowLayout parent, int position, Category item)
                {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(
                            R.layout.expend_select_item, flowlayout, false);
                    tv.setText(item.getName());
                    tv.setTag(item);
                    return tv;
                }
            });
        }
    }

    public class Child {
        private CategoryGroup father;
        private int pageIndex;
        private List<Category> items;

        public Child(CategoryGroup item) {
            father = item;
            pageIndex = 0;
        }

        public void clean() {
            items.clear();
        }

        public List refresh() {
            if (items != null)
                items.clear();
            items = DatabaseHelper.instance().getCategoryByGroup(father.getId(), pageIndex);
            return items;
        }

        public List load() {
            if (items == null)
                items = DatabaseHelper.instance().getCategoryByGroup(father.getId(), pageIndex);
            return items;
        }

        public List loadMore() {
            pageIndex++;
            long condition = APP.ENGINE_ALL;
            List data = DatabaseHelper.instance().getEngineByBrand(father.getId(), condition, pageIndex);
            items.addAll(data);
            return items;
        }

        public Category get(int position) {
            if (items == null)
                return null;
            return items.get(position);
        }

        public List list() {
            return items;
        }
    }

    public class Group {
        private int pageIndex;
        private List<CategoryGroup> items;
        private SparseArray<Child> childs;

        public Group() {
            pageIndex = 0;
            childs = new SparseArray<Child>();
        }

        public void refresh() {
            clean();
            loadData();
        }

        public void clean() {
            items.clear();
            for (int i = 0; i < childs.size() ; i++) {
                childs.get(i).clean();
            }
            childs.clear();
        }

        public void loadData() {
            long category= getCategoryId();
            if (items != null)
                items.clear();
            items = DatabaseHelper.instance().getGroupByCategory(category, pageIndex);
        }

        public List refreshChild(int groupPosition) {
            Child child = childs.get(groupPosition);
            if (child == null) {
                CategoryGroup group = get(groupPosition);
                child = new Child(group);
                childs.put(groupPosition, child);
            }
            return child.refresh();
        }

        public List loadChild(int groupPosition) {
            return loadChild(groupPosition, false);
        }

        public List loadChild(int groupPosition, boolean more) {
            Child child = childs.get(groupPosition);
            if (child == null) {
                CategoryGroup group = get(groupPosition);
                child = new Child(group);
                childs.put(groupPosition, child);
            }
            return more ? child.loadMore() : child.load();
        }

        public CategoryGroup get(int groupPosition) {
            if (items == null)
                return null;
            return items.get(groupPosition);
        }

        public List getChild(int groupPosition) {
            return loadChild(groupPosition);
        }

        public Category getChild(int groupPosition, int ChindPosition) {
            List list = loadChild(groupPosition);
            return (Category) list.get(ChindPosition);
        }

        public int getGroupCount() {
            return items.size();
        }

        public int getChildrenCount(int groupPosition) {
            //return group.getChild(groupPosition).size();
            return 1;
        }

        public int getChildCount(int groupPosition) {
            return group.getChild(groupPosition).size();
        }

        public List list() {
            return items;
        }
    }
}

