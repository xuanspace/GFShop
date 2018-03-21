package com.topway.fine.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.topway.fine.R;
import com.topway.fine.adapter.PhotoPublishAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppConfig;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Brand;
import com.topway.fine.model.Category;
import com.topway.fine.model.Engine;
import com.topway.fine.model.Photo;
import com.topway.fine.model.Product;
import com.topway.fine.model.Serie;
import com.topway.fine.ui.NumberProgressBar;
import com.topway.fine.utils.BitmapUtil;
import com.topway.fine.utils.FileUtils;
import com.topway.fine.utils.ImageCompressor;

import net.bither.util.NativeUtil;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 添加编辑产品
 * @version 1.0
 * @created 2017-3-1
 * @aouthor linweixuan@gmial.com
 */
public class ProductEditActivity extends BaseActivity {

    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_INDEX = "extra_index";

    @Bind(R.id.scrollView) ScrollView scrollView;
    @Bind(R.id.tv_brand_name) TextView tv_brand;
    @Bind(R.id.tv_serie_name) TextView tv_serie;
    @Bind(R.id.tv_engine_name) TextView tv_engine;
    @Bind(R.id.tv_categry_name) TextView tv_category;
    @Bind(R.id.tv_categry_path) TextView tv_category_path;
    @Bind(R.id.et_product_name) EditText tv_name;
    @Bind(R.id.et_product_description) EditText tv_description;
    @Bind(R.id.et_price) EditText et_price;
    @Bind(R.id.et_quntity) EditText et_quntity;
    @Bind(R.id.gv_photo) GridView gv_photo;

    @Bind(R.id.re_brand) RelativeLayout re_brand;
    @Bind(R.id.re_serie) RelativeLayout re_serie;
    @Bind(R.id.re_engine) RelativeLayout re_engine;
    @Bind(R.id.re_category) RelativeLayout re_category;
    @Bind(R.id.re_upload) RelativeLayout re_upload;

    @Bind(R.id.btn_save) Button btn_save;

    @Bind(R.id.tv_uploadSize) TextView tv_uploadSize;
    @Bind(R.id.tv_netSpeed) TextView tv_netSpeed;
    @Bind(R.id.tv_progress) TextView tv_progress;
    @Bind(R.id.pb_progressBar) NumberProgressBar pb_progressBar;

    private ProductEditActivity context;
    private PhotoPublishAdapter adapter;
    private Brand brand;
    private Product entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product_edit);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化各个控件
     */
    private void initView() {
        context = this;
        setActivityTitle("产品");

        initScrollView();
        initViewClickListener();
        initPhotoSelectGridView();
        initScrollView();

        et_price.setInputType(EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        et_quntity.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
    }

    /**
     * 初始化滚动view事件处理
     */
    public void initScrollView() {
        scrollView.smoothScrollTo(0, 0);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    /**
     * 初始化空间点击事件处理
     */
    private void initViewClickListener() {
        re_brand.setOnClickListener(new ClickListener());
        re_serie.setOnClickListener(new ClickListener());
        re_engine.setOnClickListener(new ClickListener());
        re_category.setOnClickListener(new ClickListener());
        btn_save.setOnClickListener(new ClickListener());
    }

    /**
     * 初始化图片选择GridView
     */
    private void initPhotoSelectGridView() {
        adapter = new PhotoPublishAdapter(context);
        gv_photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_photo.setAdapter(adapter);
        gv_photo.setOnItemClickListener(new PhotoGridListener());
    }


    /**
     * 初始化界面数据
     */
    private void initData() {
        if (getOperation() == ENTITY_ADD) {
            brand = getParameter("brand", Brand.class);
        }

        if (getOperation() == ENTITY_EDIT) {
            entity = getParameter(Product.class);
            setActivityTitle(entity.getName());
        }

        if (entity != null) {
            entity.setDefaultValue();
            tv_name.setText(entity.getName());
            et_price.setText(entity.getPrice().toString());
            et_quntity.setText(entity.getQuantity().toString());

            brand = entity.getBrand();
            if (brand != null) {
                tv_brand.setText(brand.getName());
                tv_brand.setTag(brand.getId());
            }

            Category category = entity.getCategory();
            if (category != null) {
                tv_category.setText(category.getName());
                tv_category.setTag(category.getId());
            }

            Engine engine = entity.getEngine();
            if (engine != null) {
                tv_engine.setText(engine.getName());
                tv_engine.setTag(engine.getId());
            }
        }
        else{
            if (brand != null) {
                tv_brand.setText(brand.getName());
                tv_brand.setTag(brand.getId());
            }
        }
    }

    /**
     * 显示品牌选择界面
     */
    private void showBrandSelectActvity() {
        Intent intent = new Intent(this, BrandSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_BRAND_SELECT);
    }

    /**
     * 品牌选择界面结果
     */
    private void getBrandSelectResult(Intent data) {
        if (data != null) {
            brand = data.getParcelableExtra("entity");
            tv_brand.setText(brand.getName());
            tv_brand.setTag(brand.getId());

            setProductName();
        }
    }

    /**
     * 机型选择界面结果
     */
    private void showSerieSelectActvity() {
        Intent intent = new Intent(this, SerieSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_SERIE_SELECT);
    }

    /**
     * 机型选择界面结果
     */
    private void getSerieSelectResult(Intent data) {
        if (data != null) {
            Serie item = data.getParcelableExtra("entity");
            tv_serie.setText(item.getName());
            tv_serie.setTag(item.getId());

            setProductName();
        }
    }

    /**
     * 显示发动机选择界面
     */
    private void showEngineSelectActvity() {
        Intent intent = new Intent(this, EngineSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_ENGINE_SELECT);
    }

    /**
     * 发动机选择界面结果
     */
    private void getEngineSelectResult(Intent data) {
        if (data != null) {
            Engine item = data.getParcelableExtra("entity");
            tv_engine.setText(item.getName());
            tv_engine.setTag(item.getId());
            setProductName();
        }
    }

    /**
     * 显示部件类型选择界面
     */
    private void showCategorySelectActvity() {
        Intent intent = new Intent(this, CategoryIndexActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("operation", ENTITY_SELECT);
        intent.putExtras(bundle);
        startActivityForResult(intent, APP.REQUEST_CATEGORY_SELECT);
    }

    /**
     * 部件类型选择界面结果
     */
    private void getCategorySelectResult(Intent data) {
        if (data != null) {
            Category item = data.getParcelableExtra("entity");
            tv_category.setText(item.getName());
            tv_category.setTag(item.getId());
            tv_category_path.setText(item.getPath());
            setProductName();
        }
    }

    /**
     * 设置描述字符串用了搜索
     */
    private void setProductName() {

        String desc = tv_brand.getText().toString() + " " +
                        tv_serie.getText().toString() + " " +
                        tv_engine.getText().toString() + "-" +
                        tv_category.getText().toString();
        tv_name.setText(desc);
    }


    /**
     * 产品输入项箭头事件处理
     */
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.re_brand:
                    showBrandSelectActvity();
                    break;
                case R.id.re_engine:
                    showEngineSelectActvity();
                    break;
                case R.id.re_category:
                    showCategorySelectActvity();
                    break;
                case R.id.re_serie:
                    showSerieSelectActvity();
                    break;
                case R.id.btn_save:
                    saveData();
                    break;
            }
        }
    }

    /**
     * 图片网格项点击事件处理
     */
    private class PhotoGridListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (adapter.isAddMore(position)) {
                showPhotoSelectActivity();
            }
            else{
                showPhotoPreviewActivity();
            }
        }
    }

    /**
     * 显示图片选择界面
     */
    public void showPhotoSelectActivity() {
        Intent intent = new Intent(this, PhotoSelectActivity.class);
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_SELECT);
    }

    /**
     * 显示预览界面
     */
    public void showPhotoPreviewActivity() {
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_DATA, adapter.getData());
        intent.putExtra(EXTRA_INDEX, adapter.getSelectedPosition());
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_PREVIEW);
    }

    /**
     * 界面选择结果
     */
    public void getPhotoSelectResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.addData(images);
        }
    }

    /**
     * 图片预览结果
     */
    public void getPhotoPreviewResult(Intent data) {
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(EXTRA_DATA);
            adapter.setData(images);
        }
    }

    /**
     * 将字符类型的请求参数转换成整数
     * @param requestCode 请求码
     * @param resultCode  返回代码
     * @return data 返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case APP.REQUEST_BRAND_SELECT:
                    getBrandSelectResult(data);
                    break;
                case APP.REQUEST_SERIE_SELECT:
                    getSerieSelectResult(data);
                    break;
                case APP.REQUEST_ENGINE_SELECT:
                    getEngineSelectResult(data);
                    break;
                case APP.REQUEST_CATEGORY_SELECT:
                    getCategorySelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_SELECT:
                    getPhotoSelectResult(data);
                    break;
                case APP.REQUEST_PHOTO_PREVIEW:
                    getPhotoPreviewResult(data);
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 将创建的新产品保存到数据库
     */
    public void saveData() {
        // 检查是否存在
        DatabaseHelper helper = DatabaseHelper.instance();
        String name = tv_name.getText().toString();
        long brandId = getID(tv_brand);
        long serieId = getID(tv_serie);
        long categoryId = getID(tv_category);
        long engineId = getID(tv_engine);
        long partId = 0;

        // 部件的名字不能重复
        if (getOperation() == ENTITY_ADD) {
            Product found = helper.findProduct(name,brandId,categoryId,engineId,serieId);
            if (found != null) {
                //tv_tip.setText("部件名已经存在!");
                return;
            }
        }

        // 不是修改原有项
        if (entity == null) {
            entity = new Product();
            entity.setId(helper.getNextSequnce());
        }

        // 各个关联ID
        entity.setBrandId(getID(tv_brand));
        entity.setSerieId(serieId);
        entity.setEngineId(getID(tv_engine));
        entity.setCategoryId(getID(tv_category));
        entity.setPartId(new Long(0));

        // 产品主要要素
        entity.setName(getStr(tv_name));
        entity.setPath(getOidPath());
        entity.setPrice(getFloat(et_price));
        entity.setQuantity(getInt(et_quntity));
        entity.setPath(getStr(tv_category_path));
        entity.setDescription(getStr(tv_description));

        // 设置产品日期
        Date date = new Date();
        entity.setUptime(date.getTime());

        // 先后端保存,获取主键ID,保存主键到本地
        entity.setDefaultValue();

        // 上传文件并保存
        saveProductWithPhotos(entity);

    }

    /**
     * 保存产品的图片到数据库
     * @param product  产品
     */
    public void saveProductWithPhotos(Product product) {
        // 已选择的图片路径
        boolean firstSave = false;
        ArrayList<String> images = adapter.getData();
        if(images == null || images.size() < 1)
            return;

        // 创建图片File对象
        ArrayList<File> files = new ArrayList<>();
        if (images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                // 压缩图片文件
                String filePath = images.get(i);
                File compressFile = compressImages(filePath);
                files.add(compressFile);

                // 获取文件MD5名
                filePath = compressFile.getPath();
                String extension = filePath.substring(filePath.lastIndexOf("."));
                String fileName = FileUtils.getFileMd5(filePath) + extension;
                String url = AppConfig.HTTP_DOMAIN + "/uploads/" + fileName;

                // 保存产品第一个图片
                if (!firstSave) {
                    product.setImage(url);
                    long productId = DatabaseHelper.instance().saveProduct(product);
                    product.setId(productId);
                    firstSave = true;
                }

                // 保存产品图片
                Photo enity = new Photo();
                enity.setTableId(APP.PRODUCT_TABLE);
                enity.setPrimaryId(product.getId());
                enity.setUrl(url);
                DatabaseHelper.instance().savePhoto(enity);
            }
        }

        // 上传图片文件
        uploadImages(files);
    }


    /**
     * 对原始压缩图片进行压缩
     * @param filePath  图片路径
     */
    public File compressImages(String filePath) {
        File newFile = new File(AppConfig.DEFAULT_IMAGE_PATH + "compress_" + System.currentTimeMillis() + ".jpg");
        try {
            Bitmap bitmap = BitmapUtil.getBitmap(filePath);
            NativeUtil.compressBitmap(bitmap, newFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }


    /**
     * 对原始压缩图片进行压缩
     * @param filePath  图片路径
     */
    public File compressImages2(String filePath) {
        File origninalFile = new File(filePath);
        return ImageCompressor.getDefault(context).compress(origninalFile);
    }

    /**
     * 保存产品的图片到数据库
     * @param files  产品图片
     */
    public void uploadImages(ArrayList<File> files) {
        // 显示上传的进度条
        re_upload.setVisibility(View.VISIBLE);
        pb_progressBar.setVisibility(View.VISIBLE);
        btn_save.setEnabled(false);

        // 文件上传处理
        OkGo.post(AppConfig.HTTP_DOMAIN + "/photo")
                .tag(this)
                .addFileParams("files[]", files)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        btn_save.setText("正在保存...");
                    }

                    @Override
                    public void onSuccess(String responseData, Call call, Response response) {
                        btn_save.setText("保存完成");
                        context.finish();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        handleUploadError(call, response, e);
                    }

                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
                        String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
                        tv_uploadSize.setText(downloadLength + "/" + totalLength);
                        String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
                        tv_netSpeed.setText(netSpeed + "/S");
                        tv_progress.setText((Math.round(progress * 10000) * 1.0f / 100) + "%");
                        pb_progressBar.setMax(100);
                        pb_progressBar.setProgress((int) (progress * 100));
                    }
                });
    }

    /**
     * 上传产品的图片错误处理
     * @param call
     * @param response
     */
    private void handleUploadError(Call call, Response response, Exception e) {
        btn_save.setText("保存出错,重新保存");
        btn_save.setEnabled(true);
    }

    /**
     * 关闭界面并返回数据
     * @param item
     */
    public void finishResult(Product item) {
        Intent intent = new Intent();
        intent.putExtra("operation", getOperation());
        intent.putExtra("position", getPosition());
        intent.putExtra("entity", item);
        setResult(RESULT_OK, intent);
        context.finish();
    }

}
