package com.topway.fine.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.app.AppConfig;
import com.topway.fine.app.AppManager;
import com.topway.fine.model.Brand;
import com.topway.fine.utils.FileUtils;
import com.topway.fine.utils.StringUtils;

import java.io.File;

/**
 * Activity基础类,对Activity进行管理
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class BaseActivity extends FragmentActivity {

    public static final int MIN_CLICK_DELAY_TIME = 500;

    // Acitity之间传递参数和操作
    protected static final int ENTITY_NOOP = 0;
    protected static final int ENTITY_ADD = 1;
    protected static final int ENTITY_EDIT = 2;
    protected static final int ENTITY_PID = 3;
    protected static final int ENTITY_PART = 4;
    protected static final int ENTITY_SELECT = 5;
    protected static final int ENTITY_KEY = 5;
    protected static final int ENTITY_NOPOS = -1;

    private TextView tv_title;
    private int operation = 0;
    private Parcel parameter;
    private String oidPath;
    protected Handler mHandler;
    protected long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }

        // 设置系统消息提示栏颜色
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        //tintManager.setStatusBarTintResource(R.color.status_bar_bg);//通知栏所需颜色

        // 初始化消息处理
        initHandler();

        // 初始化Acitivty的基本view
        initBaseView();

        // 获取Acitivty的启动参数
        getBundle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 初始化Activity各个view
    */
    private void initBaseView() {

    }

    /**
     * 初始化Activity消息处理
     */
    protected void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                onEvent(msg);
            }
        };
    }

    /**
     * Activity消息处理
     */
    protected void onEvent(Message msg) {
    }

    /**
     * 判断是否快速点击
     */
    public synchronized boolean isDoubleClick() {
        long currentTime = System.currentTimeMillis();
        if ( currentTime - mLastClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        mLastClickTime = currentTime;
        return false;
    }

    /**
     * 显示Activity
     */
    protected void finishActivity() {
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 显示Activity
     *
     * @param cls 显示Activity的class
     */
    public void showActivity(Class<?> cls) {
        showActivity(cls, null, ENTITY_NOOP);
    }

    /**
     * 显示Activity，缺省只传递对象，不指示动作
     *
     * @param cls 显示Activity的class
     * @param param 任意对象
     */
    public void showActivity(Class<?> cls, Object param) {
        showActivity(cls, param, ENTITY_NOOP);
    }

    /**
     * 显示Activity，缺省只传递对象和操作指示
     *
     * @param cls 显示Activity的class
     * @param param 任意对象
     * @param operation 指示何种动作
     */
    public void showActivity(Class<?> cls, Object param, int operation) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)param);
        bundle.putInt("operation", operation);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    /**
     * 显示Activity，缺省只传递对象和操作指示
     *
     * @param cls 显示Activity的class
     * @param param 任意对象
     * @param operation 指示何种动作
     */
    public void showActivityForResult(Class<?> cls, Object param, int operation) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)param);
        bundle.putInt("operation", operation);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, operation);
    }

    public void showActivityForResult(Class<?> cls, int operation) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", operation);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        this.startActivityForResult(intent, operation);
    }

    public void finishActivityResult(Parcelable item) {
        Intent intent = new Intent();
        intent.putExtra("operation", getOperation());
        intent.putExtra("position", getPosition());
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        this.finish();
    }

    public Handler getHandler() {
        return mHandler;
    }

    private void getBundle() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            //parameter = bundle.getParcelable("entity");
            operation = bundle.getInt("operation");
            oidPath = bundle.getString("oidpath");
            if (oidPath == null)
                oidPath = "";
        }
    }

    protected final <T> T getParameter(String key, Class<T> clazz) {
        T entity = null;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            entity = bundle.getParcelable(key);
        return entity;
    }

    protected final <T> T getParameter(Class<T> clazz) {
        T entity = null;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            entity = bundle.getParcelable("entity");
        return entity;
    }

    public int getPrimaryKey() {
        int keyId = -1;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            keyId = bundle.getInt("keyid");
        return keyId;
    }

    public int getPosition() {
        int position = -1;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            position = bundle.getInt("position");
        return position;
    }

    protected int getOperation() {
        return operation;
    }

    public String getOidPath() {
        return oidPath;
    }

    public void setOidPath(String oidPath) {
        this.oidPath = oidPath;
    }

    public String getStr(TextView view) {
        return view.getText().toString();
    }

    public String getStr(EditText view) {
        return view.getText().toString();
    }

    public long getInt(TextView view) {
        return Long.parseLong(view.getText().toString());
    }

    public long getInt(EditText view) {
        return Long.parseLong(view.getText().toString());
    }

    public Float getFloat(EditText view) {
        String value = view.getText().toString();
        return Float.parseFloat(value);
    }

    public long getID(TextView view) {
        Object v = view.getTag();
        return (v == null) ? 0 : (long)v;
    }

    public long getID(EditText view) {
        Object v = view.getTag();
        return (v == null) ? 0 : (long)v;
    }

    public String getUrl(ImageView view) {
        Object v = view.getTag();
        return (v == null) ? "" : (String)v;
    }

    /**
     * 加载图片文件要转成绝对路径
     *
     * @param view 图片view
     * @param imageUrl 图片的相对路径
     */
    public void setImageUrl(ImageView view, String imageUrl) {
        String url = AppConfig.IMAGE_DOMAIN;
        if (imageUrl != null)
            url = url + imageUrl;

        view.setTag(url);
        Picasso.with(this).load(new File(url))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .tag(this)
                .into(view);
    }

    /**
     * 加载图片文件要转成绝对路径
     *
     * @param draweeView 图片view
     * @param imageUrl 图片的相对路径
     */
    public void setImageUrl(SimpleDraweeView draweeView, String imageUrl) {
        String url = AppConfig.IMAGE_DOMAIN;
        if (imageUrl != null)
            url = url + imageUrl;

        Uri uri = Uri.parse(imageUrl);
        draweeView.setTag(imageUrl);
        draweeView.setImageURI(uri);
    }

    /**
     * 拷贝和计算图片文件MD5并以MD5命名,返回图片相对路径
     *
     * @param imagePath 本地图片绝对路径
     * @param dir 图片保存的相对目录
     * @return String 图片保存的相对路径
     */
    public String saveImage(String imagePath, String dir)
    {
        // 如果图片路径是空给缺省路径
        if (StringUtils.isEmpty(imagePath))
            return dir;

        // 判断是否本地brand目录的图片
        if (imagePath.indexOf(AppConfig.IMAGE_DOMAIN) != -1) {
            int start = imagePath.lastIndexOf("/");
            return dir + imagePath.substring(start);
        }

        // 拷贝图片到服务器,改名为文件自己MD5值防止图片重复
        String fileName = FileUtils.copyImageFile(imagePath, AppConfig.IMAGE_DOMAIN + dir);
        if (StringUtils.isEmpty(fileName))
            return dir;

        // 返回图片相对路径
        return dir+fileName;
    }

    public void setActivityTitle(int resource) {
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (tv_title != null)
            tv_title.setText(this.getString(resource));
    }

    public void setActivityTitle(String name) {
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (tv_title != null)
            tv_title.setText(name);
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Translucent navigation bar
        // window.setFlags(
        //      WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
        //      WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }
}
