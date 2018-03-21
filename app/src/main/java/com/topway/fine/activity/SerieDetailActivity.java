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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topway.fine.R;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Serie;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 添加品牌型号系列：编辑和添加
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class SerieDetailActivity extends BaseActivity {

    private SerieDetailActivity context;

    @Bind(R.id.ev_name) EditText ev_name;
    @Bind(R.id.tv_hot) TextView ev_hot;
    @Bind(R.id.iv_logo) ImageView iv_logo;
    @Bind(R.id.tv_tip) TextView tv_tip;
    @Bind(R.id.re_logo) LinearLayout re_logo;
    @Bind(R.id.re_engine) RelativeLayout re_engine;
    @Bind(R.id.tv_engine) TextView tv_engine;
    @Bind(R.id.btn_save) Button btn_save;

    private String imageName;
    private Serie detail;
    private Brand brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        context = this;
        re_engine.setOnClickListener(new ClickListener());
        re_logo.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());
    }

    private void initData() {
        if (getOperation() == ENTITY_ADD)
            brand = getParameter(Brand.class);

        if (getOperation() == ENTITY_EDIT)
            detail = getParameter(Serie.class);

        if (detail != null) {
            detail.setDefaultValue();
            ev_name.setText(detail.getName());
            tv_engine.setText(detail.getEngineName());
            tv_engine.setTag(detail.getEngineId());
            ev_hot.setText(detail.getHot().toString());
            setImageUrl(iv_logo, detail.getImage());

            if (brand == null)
                brand = detail.getBrand();
        }

        if (brand != null)
            setActivityTitle(brand.getName());
    }


    public void loadData() {

    }

    public void saveData() {
        DatabaseHelper helper = DatabaseHelper.instance();

        if (detail == null) {
            detail = new Serie();
            detail.setId(helper.getNextSequnce());
            detail.setCommon(new Long(0));
        }

        detail.setName(getStr(ev_name));
        detail.setBrandId(brand.getId());
        detail.setBrandName(brand.getName());
        detail.setEngineId(getID(tv_engine));
        detail.setEngineName(getStr(tv_engine));
        detail.setPath(brand.getPath());
        detail.setHot(getInt(ev_hot));

        // 图片需要先保存才知道真正的文件名
        String filename = getUrl(iv_logo);
        String url = saveImage(filename, "/serie/");
        detail.setImage(url);

        // 先后端保存,获取主键ID,保存主键到本地
        detail.setDefaultValue();
        helper.saveSerie(detail);
        finishActivityResult(detail);
    }

    private void showEngineSelectActvity() {
        Intent intent = new Intent(this, EngineListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_ENGINE_SELECT);
    }

    private void showEngineSelectActvity2() {
        Intent intent = new Intent(this, EngineSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_ENGINE_SELECT);
    }

    private void getEngineSelectResult(Intent data) {
        if (data != null) {
            Engine item = data.getParcelableExtra("entity");
            tv_engine.setText(item.getName());
            tv_engine.setTag(item.getId());
        }
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_engine:
                    showEngineSelectActvity2();
                    break;
                case R.id.re_logo:
                    showPhotoDialog();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    private void getImageSelectResult(Intent data) {
        if (data != null) {
            Uri uri = (Uri)data.getData();
            if (uri.getPath().length() > 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                //tv_tip.setVisibility(View.INVISIBLE);
                iv_logo.setImageBitmap(bitmap);
                iv_logo.setTag(uri.getPath());
            }else {
                //tv_tip.setVisibility(View.VISIBLE);
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
                    getImageSelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_CUT:
                    break;
                case APP.REQUEST_ENGINE_SELECT:
                    getEngineSelectResult(data);
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

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File("/sdcard/guangfu/brand/", imageName)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, APP.REQUEST_PHOTO_CUT);
    }
}