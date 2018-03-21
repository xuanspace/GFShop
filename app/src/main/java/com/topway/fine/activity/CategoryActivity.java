package com.topway.fine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.ListActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.CategoryDao;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Manufacturer;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.quickadapter.BaseAdapterHelper;
import com.topway.fine.utils.StringUtils;

import java.util.List;
import java.util.Stack;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 分类目录：品牌，部件等分类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryActivity extends ListActivity<Category> {

    @Bind(R.id.re_category_bar) RelativeLayout re_category_bar;
    @Bind(R.id.iv_category_logo) ImageView iv_category_logo;
    @Bind(R.id.tv_category_add) ImageView tv_category_add;
    @Bind(R.id.tv_category_sub) TextView tv_category_sub;
    @Bind(R.id.tv_category_name) TextView tv_category_name;
    @Bind(R.id.tv_category_title) TextView tv_category_title;

    private CategoryActivity context;
    private long curCategory;
    private long fatherCategory;
    private Category category;
    private Engine engine;
    private Serie serie;
    private Stack stack = new Stack();
    private String currentPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setActivityTitle(R.string.category_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        setDataSource(CategoryDao.TABLENAME);
        setItemView(R.layout.list_category_item);
        re_category_bar.setOnClickListener(new ClickListener());
        tv_category_add.setOnClickListener(new ClickListener());
        initCategory();
    }

    @Override
    protected void initItem(BaseAdapterHelper helper, Category item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setImageUrl(R.id.iv_logo, item.getImage());

        // 设置列表项点击事件
        setMenu(helper, item);
    }

    public void initCategory() {
        // 当前缺省分类是根分类目录（1）
        curCategory = APP.ROOT_CATEGORY;
        fatherCategory = APP.ROOT_CATEGORY;

        // 获取启动时的指定分类
        Category param =  getParameter(Category.class);
        if (param != null) {
            curCategory = param.getId();
        }

        // 是从发动机型号哪来的
        engine =  getParameter("engine", Engine.class);
        if (engine != null) {
            curCategory = APP.CATEGORY_ENGINE_PART;
        }

        // 是从挖机机型哪来的
        serie =  getParameter("serie", Serie.class);
        if (serie != null) {
            //curCategory = APP.CATEGORY_ENGINE_PART;
        }

        // 获取启动的对象OID路径
        currentPath = getOidPath();
    }

    private void setViewsValue(Category item) {
        tv_category_name.setText(item.getName());
        tv_category_title.setText(item.getName() + "列表：");
        setImageUrl(iv_category_logo,item.getImage());

        // 是从发动机型号哪来的，显示发动机型号
        if (engine != null) {
            tv_category_sub.setText(engine.getBrandName() + engine.getName());
        }

        // 是从挖机机型哪来的，显示挖机型号
        if (serie != null) {
            tv_category_sub.setText(serie.getBrandName() + serie.getName());
        }
    }


    @Override
    public void initData() {
        // 设置列表选择模式
        if (getOperation() == ENTITY_SELECT)
            setSelectMode(true);

        // 设置当前的分类实体
        category = DatabaseHelper.instance().getCategory(curCategory);
        if (category != null) {
            setViewsValue(category);
        }
        super.initData();
    }

    @Override
    public List getData(int page) {
        return DatabaseHelper.instance().getSubCategory(curCategory, page);
    }

    @Override
    public void refresh() {
        Category item = DatabaseHelper.instance().getCategory(curCategory);
        if (item != null) {
            category = item;
            setViewsValue(category);
            super.refresh();
        }
    }

    private void setCategoryPath(Category item) {
        // 设置选择的部件OID路径
        String path = currentPath;
        if (StringUtils.isNotEmpty(path) ) {
            path += "." + item.getPath();
            if (item.getPath().endsWith("."))
                path += item.getId().toString();
            else
                path += "." + item.getId().toString();
            setOidPath(path);
        }
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_category_bar:
                    // 点击标题栏修改分类目录项，不是列表项不带Position
                    showEditActivity(CategoryDetailActivity.class, category);
                    break;
                case R.id.tv_category_add:
                    showAddActivity(CategoryDetailActivity.class, category);
                    break;
            }
        }
    }

    // 通过分类的class显示Activity，无则返回false
    protected boolean showActivityByClass(Category item) {
        try {
            String name = item.getClazz();
            if (!StringUtils.isEmpty(name)) {
                Class<?> activity = Class.forName(name);
                showActivity(activity, item);
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    public void showPartActivity(Category category) {
        Intent intent = new Intent(this, PartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)category);

        // 添加更多参数
        if (engine != null)
            bundle.putParcelable("engine", (Parcelable)engine);
        if (serie != null)
            bundle.putParcelable("serie", (Parcelable)serie);

        // 设置对象路径
        bundle.putInt("operation", ENTITY_NOOP);
        bundle.putString("oidpath", getOidPath());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    protected void OnItemClick(Category item, int position) {
        // 判断当前分类指定界面,无则继续
        if (showActivityByClass(item)) {
            return;
        }

        // 判断是否为目录(目录没有.后缀)
        String path = item.getPath();
        if (path != null && path.endsWith(".")) {
            // 挖机部件的路径则显示部件界面
            if (path.startsWith("1.3.")) {
                setCategoryPath(item);
                showPartActivity(item);
            }
        }
        else{
            // 当前是目录，则进入下一个子分类
            stack.push(curCategory);
            fatherCategory = curCategory;
            curCategory = item.getId();
            refresh();
            return;
        }
    }

    // 点击添加新的分类
    @Override
    protected void OnItemAddClick() {
        // 获取当前的分类作为新分类的父亲
        showAddActivity(CategoryDetailActivity.class, category);
    }

    // 编辑选择的分类
    @Override
    protected void OnItemEditClick(Category item, int position) {
        showEditActivity(CategoryDetailActivity.class, item);
    }

    @Override
    protected void OnItemDeleteClick(Category item, int position) {
        DatabaseHelper.instance().deleteCategory(item);
    }

    @Override
    protected void OnItemMoveTop(Category item, int position) {
        //DatabaseHelper.instance().setCategoryTop(item);
        super.OnItemMoveTop(item, position);
    }

    @Override
    protected void OnItemSetCommon(Category item, int position) {
        //DatabaseHelper.instance().setBrandCommon(item);
    }

    @Override
    protected void OnItemChoose(Category item, int position) {
        Intent intent = new Intent();
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
    }

    // 编辑后返回的分类实体
    @Override
    protected void OnItemEditUpdate(Category item, int position) {
        if (position == ENTITY_NOPOS) {
            tv_category_name.setText(item.getName());
            tv_category_title.setText(item.getName());
        }
        // 编辑后传来的实体与当前的不一样，重新赋值
        category = item;
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
        // 调用底层的长按事件处理
        if (super.onKeyDown(keyCode, event) == false)
            return false;

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (stack.size() > 0) {
                fatherCategory = (long)stack.pop();
                curCategory = fatherCategory;
                refresh();
                return false;
            }
        }
        return true;
    }
}