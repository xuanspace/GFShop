package com.topway.fine.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import com.topway.fine.R;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.ui.loadmore.LoadMoreListView;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.ui.quickadapter.QuickAdapter;


/**
 * 基于List的Activity基础类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ListActivity2<T> extends BaseActivity {

    private static final String TAG ="ListActivity";

    ImageView addView;
    ImageView editView;
    ImageView delView;
    TextView checkView;
    LoadMoreListView listView;
    private ArrayList<CheckBox> selects;
    private ListActivity2<T> context;
    protected QuickAdapter<T> adapter;
    private int pageIndex = 0;
    private int pageSize = DatabaseHelper.PAGE_SIZE;
    private boolean isLoadAll = false;
    private int itemLayoutResID;
    private String dataSource;
    private boolean isSelectMode = false;
    private boolean isLongClickMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 设置列表的项的Layout
    protected void setItemView(int layoutResID) {
        itemLayoutResID = layoutResID;
        initView();
    }

    public ImageView getAddView() {
        return addView;
    }

    // 设置列表选择模式
    public void setSelectMode(boolean selectMode) {
        isSelectMode = selectMode;
        checkView.setVisibility(View.VISIBLE);
        addView.setVisibility(View.GONE);
    }

    // 设置要访问的数据表名
    protected void setDataSource(String name) {
        dataSource = name;
    }

    protected String getDataSource() {
        return dataSource;
    }

    public LoadMoreListView getListView() {
        return listView;
    }

    private void initView() {
        context = this;
        addView = (ImageView) this.findViewById(R.id.iv_add);
        editView = (ImageView) this.findViewById(R.id.iv_edit);
        delView = (ImageView) this.findViewById(R.id.iv_delete);
        checkView = (TextView) this.findViewById(R.id.tv_checked);
        listView = (LoadMoreListView) this.findViewById(R.id.listview);

        // 设置选择项
        selects = new ArrayList<CheckBox>();

        // 设置列表项
        adapter = new QuickAdapter<T>(this, itemLayoutResID) {
            @Override
            protected void convert(BaseAdapterHelper helper, T item) {
                initItem(helper, item);
            }
        };

        listView.setAdapter(adapter);

        // 加载更多数据
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadListData();
            }
        });

        // 点击事件
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                T item = (T) listView.getItemAtPosition(position);
                OnItemClick(item, position);
            }
        });

        // 滚动事件
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    Picasso.with(context).pauseTag(context);
                } else {
                    Picasso.with(context).resumeTag(context);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        // 添加按钮事件
        addView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemAddClick();
            }
        });

        // 编辑按钮事件
        editView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemEditClick(null, 0);
            }
        });

        // 删除按钮事件
        delView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemDeleteClick(null, 0);
            }
        });

        // 选择按钮事件
        checkView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                T item = getCheckedItem();
                OnItemChoose(item, 0);
            }
        });

    }

    // 上层Activity有时候需要先做自己的一些数据初始化，
    // 重载后记得调用super.initData()加载数据
    public void initData() {
        initListData();
    }

    // 刷新列表首页数据
    private void initListData() {
        pageIndex = 0;
        if (isLoadAll == false) {
            List data = getData(pageIndex);
            listView.setVisibility(View.VISIBLE);
            adapter.replaceAll(data);
            if (data.size() < pageSize)
                isLoadAll = true;
            listView.updateLoadMoreViewText(data);
        }
    }

    // 列表分页加载数据
    public void loadListData() {
        if (isLoadAll == false) {
            ++pageIndex;
            List data = getData(pageIndex);
            adapter.addAll(data);
            if (data.size() < pageSize)
                isLoadAll = true;
            listView.updateLoadMoreViewText(data);
        }
    }

    // 将数据获取分离
    public List getData(int page) {
        return DatabaseHelper.get(dataSource, page);
    }

    // 从新刷新数据
    public void refresh() {
        isLoadAll = false;
        this.initListData();
    }

    public T getCheckedItem() {
        if (selects.size() > 0) {
            return (T)selects.get(0).getTag();
        }
        return null;
    }

    // 设置CheckBox为单选
    protected void setSingleCheck(CheckBox checkBox) {
        if (selects.size() > 0) {
            for (int i=0; i<selects.size(); i++) {
                selects.get(i).setChecked(false);
            }
        }
        selects.clear();
        if (checkBox.isChecked())
            selects.add(checkBox);
    }

    // 设置全部CheckBox为非选,并刷新列表
    protected void cleanAllCheckBox() {
        for (int i=0; i<selects.size(); i++) {
            selects.get(i).setChecked(false);
        }
        selects.clear();
        refresh();
    }

    // 设置列表项弹出菜单
    protected void setMenu(BaseAdapterHelper helper, final T item) {
        // 设置点击事件监听
        final int position = helper.getPosition();
        RelativeLayout re_parent = (RelativeLayout)helper.getView().findViewById(R.id.re_parent);
        final ImageView iv_select = (ImageView) helper.getView().findViewById(R.id.iv_select);
        final CheckBox checkBox = (CheckBox) helper.getView().findViewById(R.id.checkbox);
        checkBox.setTag(item);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setSingleCheck(checkBox);
                if (isChecked) {
                }
            }
        });

        if (isSelectMode && isLongClickMode) {
            if (iv_select != null)
                iv_select.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
        }
        else{
            if (iv_select != null)
                iv_select.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.GONE);
        }

        // 设置长按事件监听
        re_parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.v(TAG,"setOnLongClickListener");
                isLongClickMode = true;
                //showMenu(item, position);
                if (isSelectMode){
                    if (iv_select != null)
                        iv_select.setVisibility(View.GONE);
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                    setSingleCheck(checkBox);
                    refresh();
                }else{
                    showMenu(item, position);
                }
                return false;
            }
        });

        re_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"setOnClickListener");
                Log.v(TAG,"isLongClickMode="+ isLongClickMode);

                if (isLongClickMode) {
                    if (isSelectMode)
                        return;
                }

                OnItemClick(item, position);
            }
        });
    }

    protected void cancelSelectMode() {
        selects.clear();
    }

    // 绑定列表项的View数据
    protected void initItem(BaseAdapterHelper helper, T item) {

    }

    // 弹出列表项操作菜单
    private void showMenu(final T item, final int position) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(TAG,"setOnCancelListener");
                isLongClickMode = false;
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.context_menu_list);

        TextView tv_delete = (TextView) window.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.remove(item);
                OnItemDeleteClick(item, position);
                dialog.cancel();

            }
        });

        TextView tv_edit = (TextView) window.findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemEditClick(item, position);
                dialog.cancel();
            }
        });

        TextView tv_top = (TextView) window.findViewById(R.id.tv_top);
        tv_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemMoveTop(item, position);
                dialog.cancel();
            }
        });

        TextView tv_common = (TextView) window.findViewById(R.id.tv_common);
        tv_common.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemSetCommon(item, position);
                dialog.cancel();
            }
        });

        TextView tv_choose = (TextView) window.findViewById(R.id.tv_choose);
        tv_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnItemChoose(item, position);
                dialog.cancel();
            }
        });
    }

    // 显示编辑实体的界面
    public void showAddActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_ADD);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_ADD);
    }

    public void showAddActivity(Class<?> cls, Parcelable item) {
        if (item == null) return;
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", item);
        bundle.putInt("operation", ENTITY_ADD);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_ADD);
    }

    // 显示编辑实体的界面
    public void showEditActivity(Class<?> cls, T item) {
        if (item == null) return;
        showEditActivity(cls, item, ENTITY_NOPOS);
    }

    public void showEditActivity(Class<?> cls, T item, int position) {
        if (item == null) return;
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)item);
        bundle.putInt("operation", ENTITY_EDIT);
        bundle.putInt("position", position);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, ENTITY_EDIT);
    }

    // 编辑实体后父亲页面刷新列表
    public void updateData(Intent intent) {
        int operation = intent.getIntExtra("operation", -1);
        int position = intent.getIntExtra("position", ENTITY_NOPOS);
        T item = intent.getParcelableExtra("entity");
        if (item != null) {
            if (operation == ENTITY_EDIT) {
                OnItemEditUpdate(item, position);
                if (position != ENTITY_NOPOS)
                    adapter.set(position, item);
            }
            else if (operation == ENTITY_ADD) {
                adapter.addTop(item);
            }
        }
    }

    // 父亲页面更新编辑后返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ENTITY_ADD:
                    updateData(intent);
                    break;
                case ENTITY_EDIT:
                    updateData(intent);
                    break;
                default:
                    break;
            }
        }
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

    protected void OnItemClick(T item, int position) {

    }

    protected void OnItemAddClick() {

    }

    protected void OnItemEditClick(T item, int position) {

    }

    protected void OnItemDeleteClick(T item, int position) {

    }

    protected void OnItemEditUpdate(T item, int position) {

    }

    protected void OnItemMoveTop(T item, int position) {
        T top = adapter.getItem(0);
        List<T> list = adapter.getData();
        list.remove(position);
        list.add(0,item);
        adapter.notifyDataSetChanged();
    }

    protected void OnItemSetCommon(T item, int position) {

    }

    protected void OnItemChoose(T item, int position) {

    }


    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isLongClickMode && isSelectMode) {
                cleanAllCheckBox();
                isLongClickMode = false;
                refresh();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

