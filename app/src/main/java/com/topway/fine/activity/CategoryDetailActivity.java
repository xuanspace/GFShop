package com.topway.fine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 添加编辑分类
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class CategoryDetailActivity extends BaseActivity {

    @Bind(R.id.tv_father) TextView tv_father;
    @Bind(R.id.ev_name) EditText ev_name;
    @Bind(R.id.tv_hot) TextView ev_hot;
    @Bind(R.id.iv_logo) ImageView iv_logo;
    @Bind(R.id.tv_tip) TextView tv_tip;
    @Bind(R.id.re_father) LinearLayout re_father;
    @Bind(R.id.re_logo) LinearLayout re_logo;
    @Bind(R.id.btn_save) Button btn_save;

    private CategoryDetailActivity context;
    private String imageName;
    private Category detail;
    private Category father;  // 上级分类目录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        setActivityTitle(R.string.category_title);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_logo.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());
    }

    private void initData() {
        // 如果添加, 传来的是父亲分类
        if (getOperation() == ENTITY_ADD)
            father = getParameter(Category.class);

        if (getOperation() == ENTITY_EDIT)
            detail = getParameter(Category.class);

        if (detail != null) {
            ev_name.setText(detail.getName());
            ev_name.setTag(detail.getId());
            ev_hot.setText(detail.getHot().toString());
            setImageUrl(iv_logo, detail.getImage());

            // 获取父亲分类的信息
            if (father == null)
                father = DatabaseHelper.instance().getCategory(detail.getPid());

            // 当前是根目录,隐藏父亲
            if (detail.getId() == APP.ROOT_CATEGORY) {
                re_father.setVisibility(View.GONE);
            }
        }

        if (father != null) {
            tv_father.setText(father.getName());
            tv_father.setTag(father.getId());
        }
    }

    public void loadData() {

    }

    public void saveData() {
        DatabaseHelper helper = DatabaseHelper.instance();

        if (detail == null) {
            detail = new Category();
            detail.setId(helper.getNextSequnce());
        }

        detail.setPid(getID(tv_father));
        detail.setName(getStr(ev_name));
        detail.setHot(getInt(ev_hot));

        // TODO: 设置分类路径
        if (getOperation() == ENTITY_ADD){
            detail.setPath("");
            detail.setRid(new Long(0));
            detail.setGid(new Long(0));
        }

        // 图片需要先保存才知道真正的文件名
        String filename = getUrl(iv_logo);
        String url = saveImage(filename, "/category/");
        detail.setImage(url);

        // 先后端保存,获取主键ID,保存主键到本地
        detail.setDefaultValue();
        helper.saveCategory(detail);
        finishActivityResult(detail);
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_logo:
                    showPhotoDialog();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    public void showPhotoDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, APP.REQUEST_PHOTO_GALLERY);
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_PHOTO_GALLERY:
                    if (data != null) {
                        Uri uri = (Uri)data.getData();
                        if (uri.getPath().length() > 0) {
                            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                            //tv_tip.setVisibility(View.INVISIBLE);
                            iv_logo.setImageBitmap(bitmap);
                            iv_logo.setTag(uri.getPath());
                            //startPhotoZoom(data.getData(), 480);
                        }else {
                            //tv_tip.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case APP.REQUEST_PHOTO_CUT:
                    break;

            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    @SuppressLint("SdCardPath")
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/guangfu/brand/", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, APP.REQUEST_PHOTO_CUT);
    }
}