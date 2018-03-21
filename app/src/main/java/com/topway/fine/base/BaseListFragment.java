package com.topway.fine.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.topway.fine.R;
import com.topway.fine.app.AppConfig;
import com.topway.fine.utils.FileUtils;
import com.topway.fine.utils.StringUtils;

import java.io.File;

/**
 * Fragment基础类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class BaseListFragment extends BaseFragment {

    // Fragment传递参数和操作
    protected static final int ENTITY_NOOP = 0;
    protected static final int ENTITY_ADD = 1;
    protected static final int ENTITY_EDIT = 2;
    protected static final int ENTITY_PID = 3;
    protected static final int ENTITY_PART = 4;
    protected static final int ENTITY_SELECT = 5;
    protected static final int ENTITY_KEY = 5;
    protected static final int ENTITY_NOPOS = -1;

    private Activity context;
    private int operation = 0;
    private int position = -1;
    private long oidKey = -1;
    private String oidPath;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        getStartArguments();
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
     * @param entity 任意对象
     * @param operation 指示何种动作
     */
    public void showActivity(Class<?> cls, Object entity, int operation) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)entity);
        bundle.putInt("operation", operation);
        bundle.putInt("position", position);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 显示Activity，缺省只传递对象和操作指示
     *
     * @param cls 显示Activity的class
     * @param param 任意对象
     * @param operation 指示何种动作
     */
    public void showActivityForResult(Class<?> cls, Object param, int operation) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", (Parcelable)param);
        bundle.putInt("operation", operation);
        bundle.putInt("position", position);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        startActivityForResult(intent, operation);
    }

    public void showActivityForResult(Class<?> cls, int operation) {
        Intent intent = new Intent(context, cls);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", operation);
        bundle.putString("oidpath", oidPath);
        intent.putExtras(bundle);
        startActivityForResult(intent, operation);
    }

    public void finishActivityResult(Parcelable item) {
        Intent intent = new Intent();
        intent.putExtra("operation", operation);
        intent.putExtra("position", position);
        intent.putExtra("entity", item);
        context.setResult(Activity.RESULT_OK, intent);
        context.finish();
    }

    private void getStartArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            operation = bundle.getInt("operation");
            position = bundle.getInt("position");
            oidKey = bundle.getInt("oidkey");
            oidPath = bundle.getString("oidpath");
            if (oidPath == null)
                oidPath = "";
        }
    }

    protected final <T> T getEntityArg(Class<T> clazz) {
        T entity = null;
        Bundle bundle = getArguments();
        if (bundle != null)
            entity = bundle.getParcelable("entity");
        return entity;
    }

    public int getPositionArg() {
        return position;
    }

    public void setPositionArg(int position) {
        this.position = position;
    }

    protected int getOperationArg() {
        return operation;
    }

    public long getOidKeyArg() {
        return oidKey;
    }

    public void setOidKeyArg(long key) {
        this.oidKey = key;
    }

    public String getOidPathArg() {
        return oidPath;
    }

    public void setOidPathArg(String oidPath) {
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
        Picasso.with(context).load(new File(url))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .tag(this)
                .into(view);
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
}
