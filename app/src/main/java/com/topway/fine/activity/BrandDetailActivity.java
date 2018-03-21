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
import android.widget.Toast;

import com.topway.fine.R;
import com.topway.fine.api.ApiResponse;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;

import java.io.File;

/**
 * 添加品牌：编辑品牌添加
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class BrandDetailActivity extends BaseActivity {

    private BrandDetailActivity context;
    private LinearLayout re_category;
    private LinearLayout re_zone;
    private LinearLayout re_logo;

    private EditText ev_name;
    private TextView ev_category;
    private TextView ev_zone;
    private TextView ev_hot;
    private ImageView iv_logo;
    private TextView tv_tip;
    private Button btn_save;

    private String imageName;
    private Brand detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_add);
        setActivityTitle(R.string.brand_title_add);
        initView();
        initData();
    }

    private void initView() {
        context = this;

        ev_name = (EditText) this.findViewById(R.id.name);
        ev_category = (TextView) this.findViewById(R.id.category);
        ev_zone = (TextView) this.findViewById(R.id.zone);
        ev_hot = (TextView) this.findViewById(R.id.hot);
        iv_logo = (ImageView) this.findViewById(R.id.iv_logo);
        tv_tip = (TextView) this.findViewById(R.id.tv_tip);

        re_category = (LinearLayout) this.findViewById(R.id.re_category);
        re_zone = (LinearLayout) this.findViewById(R.id.re_zone);
        re_logo = (LinearLayout) this.findViewById(R.id.re_logo);
        btn_save = (Button) this.findViewById(R.id.btn_save);

        re_category.setOnClickListener(new ClickListener());
        re_zone.setOnClickListener(new ClickListener());
        re_logo.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());

        if (getOperation() == ENTITY_EDIT)
            setActivityTitle(R.string.brand_title_edit);

    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_category:
                    showCategorySelectActvity();
                    break;
                case R.id.re_zone:
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

    private void initData() {
        if (getOperation() == ENTITY_EDIT) {
            detail = getParameter(Brand.class);
        }

        if (detail != null) {
            detail.setDefaultValue();
            ev_name.setText(detail.getName());
            ev_category.setText(detail.getCategory().getName());
            ev_category.setTag(detail.getCategoryId());
            ev_zone.setText(detail.getZone().getName());
            ev_zone.setTag(detail.getZoneId());
            ev_hot.setText(detail.getHot().toString());
            setImageUrl(iv_logo, detail.getImage());

            // hide the picture selection hint
            //if (detail.getImage().length() > 0)
            //    tv_tip.setVisibility(View.GONE);
        }
    }


    public void loadData() {

    }

    public void saveData() {

        DatabaseHelper helper = DatabaseHelper.instance();
        if (detail == null) {
            detail = new Brand();
            detail.setId(helper.getNextSequnce());
        }

        detail.setName(getStr(ev_name));
        detail.setCategoryId(getID(ev_category));
        detail.setZoneId(getID(ev_zone));
        detail.setHot(getInt(ev_hot));

        // 设置未设置分类的缺省值
        if (detail.getCategoryId() == 0)
            detail.setCategoryId(new Long(2));

        // 图片需要先保存才知道真正的文件名
        String filename = getUrl(iv_logo);
        String url = saveImage(filename, "/brand/");
        detail.setImage(url);

        // 先后端保存,获取主键ID,保存主键到本地
        detail.setDefaultValue();
        helper.saveBrand(detail);
        finishActivityResult(detail);
    }

    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            Category item = data.getParcelableExtra("entity");
            ev_category.setText(item.getName());
            ev_category.setTag(item.getId());
        }
    }

    private void getImageSelectResult(Intent intent) {
        if (intent != null) {
            Uri uri = (Uri)intent.getData();
            if (uri.getPath().length() > 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
                tv_tip.setVisibility(View.INVISIBLE);
                iv_logo.setImageBitmap(bitmap);
                iv_logo.setTag(uri.getPath());

            }else {
                tv_tip.setVisibility(View.VISIBLE);
            }
        }
    }

    public void showPhotoDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, APP.REQUEST_PHOTO_GALLERY);
    }

    // 选择品牌类型、图片结果返回
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
                case APP.REQUEST_CATEGORY_SELECT:
                    getCategorySelectResult(data);
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

    ApiResponse response = new ApiResponse(context) {
        @Override
        public void onHandle(boolean requestok) {
            if (requestok) {
                String result = getData();
                if (result.equals("success")) {
                    //afterLogin();
                }else{
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
