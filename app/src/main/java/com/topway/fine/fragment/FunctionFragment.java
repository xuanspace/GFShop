package com.topway.fine.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.fresco.helper.photoview.PictureBrowse;
import com.topway.fine.R;
import com.topway.fine.activity.BrandSelectActivity;
import com.topway.fine.activity.CategoryIndexActivity;
import com.topway.fine.activity.CategoryPickActivity;
import com.topway.fine.activity.CategorySelectActivity;
import com.topway.fine.activity.CategoryGroupSelectActivity;
import com.topway.fine.activity.CompanyIndexActivity;
import com.topway.fine.activity.EngineSelectActivity;
import com.topway.fine.activity.ManufacturerActivity;
import com.topway.fine.activity.PartQuickActivity;
import com.topway.fine.activity.PhoneContactActivity;
import com.topway.fine.activity.PhoneContactDetailActivity;
import com.topway.fine.activity.PhotoBrowseActivity;
import com.topway.fine.activity.PhotoPublishActivity;
import com.topway.fine.activity.ProductEditActivity;
import com.topway.fine.activity.ProductShowAcitivity;
import com.topway.fine.activity.ViewPagerActivity;
import com.topway.fine.adapter.FunctionAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.app.AppConfig;
import com.topway.fine.base.BaseFragment;
import com.topway.fine.base.BaseListFragment;
import com.topway.fine.db.DatabaseHelper;
import com.topway.fine.model.Category;
import com.topway.fine.model.Product;
import com.topway.fine.ui.UIHelper;
import com.topway.fine.ui.gridview.LabelGridView;
import com.topway.fine.utils.ImageCompressor;


import net.bither.util.NativeUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.topway.fine.app.AppConfig.IMAGE_DOMAIN;

/**
 * 功能页面：各个功能模块页面入口
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class FunctionFragment extends BaseFragment {

    @Bind(R.id.gridview) LabelGridView gridView;

    @Override
    public int getLayoutId() {
        return R.layout.layout_main_grid;
    }

    @Override
    public void initView() {
        gridView.setAdapter(new FunctionAdapter(mActivity));
        gridView.setOnItemClickListener(new GridViewListener(mActivity));
    }

    private void showExcavatorBrandActivity() {
        Category category = DatabaseHelper.instance().getCategory(APP.CATEGORY_EXCAVATOR);
        showActivity(ManufacturerActivity.class, category);
    }

    private void showEngineBrandActivity() {
        Category category = DatabaseHelper.instance().getCategory(APP.CATEGORY_ENGINE);
        showActivity(ManufacturerActivity.class, category);
    }

    private void showEngineSelectActivity() {
        Intent intent = new Intent(mActivity, EngineSelectActivity.class);
        mActivity.startActivity(intent);
    }

    private void showCategorySelectActivity() {
        Intent intent = new Intent(mActivity, CategoryGroupSelectActivity.class);
        mActivity.startActivity(intent);
    }

    private void showCategoryMultiSelectActivity() {
        Intent intent = new Intent(mActivity, CategorySelectActivity.class);
        mActivity.startActivity(intent);
    }

    private void showBrandMultiSelectActivity() {
        Intent intent = new Intent(mActivity, BrandSelectActivity.class);
        mActivity.startActivity(intent);
    }

    private void showPartQuickActivity() {
        Intent intent = new Intent(mActivity, PartQuickActivity.class);
        mActivity.startActivity(intent);
    }

    private void showCategoryPickActivity() {
        Intent intent = new Intent(mActivity, CategoryPickActivity.class);
        mActivity.startActivity(intent);
    }

    private void showCategoryIndexActivity() {
        Intent intent = new Intent(mActivity, CategoryIndexActivity.class);
        mActivity.startActivity(intent);
    }

    private void showContactDetailActivity() {
        Intent intent = new Intent(mActivity, PhoneContactDetailActivity.class);
        mActivity.startActivity(intent);
    }

    private void showComapnyIndexActivity() {
        Intent intent = new Intent(mActivity, CompanyIndexActivity.class);
        mActivity.startActivity(intent);
    }

    private void showPhoneContactActivity() {
        Intent intent = new Intent(mActivity, PhoneContactActivity.class);
        mActivity.startActivity(intent);
    }

    private void showPhotoPublishActivity() {
        Intent intent = new Intent(mActivity, PhotoPublishActivity.class);
        mActivity.startActivity(intent);
    }

    /**
     * 显示产品详细的信息页面(Tab形式)
     */
    private void showProductDetailActvity() {
        Intent intent = new Intent(mActivity, ProductShowAcitivity.class);
        mActivity.startActivity(intent);
    }

    /**
     * 显示添加产品页面
     */
    private void showProductEditActvity() {
        Intent intent = new Intent(mActivity, ProductEditActivity.class);
        mActivity.startActivity(intent);
    }

    public static final int REQUEST_PICK_IMAGE = 10011;
    public static final int REQUEST_KITKAT_PICK_IMAGE = 10012;

    private void imageCompress() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_KITKAT_PICK_IMAGE);
    }

    private void photoview() {
        Intent intent = new Intent(mActivity, ViewPagerActivity.class);
        mActivity.startActivity(intent);
    }

    private void photoview2() {
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(
                "http://192.168.1.100:8000/uploads/113f87444ad94dfdaf3efe248e5a55c9.jpg",
                IMAGE_DOMAIN + "/ads/ads2.jpg",
                IMAGE_DOMAIN + "/ads/ads3.jpg",
                IMAGE_DOMAIN + "/ads/ads4.jpg"));
        PictureBrowse.newBuilder(getContext(), PhotoBrowseActivity.class)
                .setPhotoStringList(items)
                .setCurrentPosition(0)
                .enabledAnimation(false)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    if (data != null) {
                        Uri uri = data.getData();
                        compressImage(uri);
                    }
                    break;
                case REQUEST_KITKAT_PICK_IMAGE:
                    if (data != null) {
                        Uri uri = ensureUriPermission(this.getContext(), data);
                        compressImage(uri);
                    }
                    break;
            }
        }
    }


    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Uri ensureUriPermission(Context context, Intent intent) {
        Uri uri = intent.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int takeFlags = intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
        }
        return uri;
    }

    public void compressImage(Uri uri) {
        try {
            Context context = getContext();
            File saveFile = new File(context.getExternalCacheDir(), "compress_" + System.currentTimeMillis() + ".jpg");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            NativeUtil.compressBitmap(bitmap, saveFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GridViewListener implements OnItemClickListener {
        private Activity mActivity = null;

        public GridViewListener(Activity activity) {
            this.mActivity = activity;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //ImageView imageView= (ImageView)arg1;
            Intent intent = new Intent();
            switch (position) {
                case 0:
                    UIHelper.showCategoryActivity(mActivity, null);
                    break;
                case 1:
                    showExcavatorBrandActivity();
                    break;
                case 2:
                    showEngineBrandActivity();
                    break;
                // 第二行
                case 3:
                    UIHelper.showBrandActivity(mActivity);
                    break;
                case 4:
                    showPartQuickActivity();
                    //showEngineSelectActivity();
                    break;
                case 5:
                    showCategorySelectActivity();
                    break;
                // 第三行
                case 6:
                    showComapnyIndexActivity();
                    break;
                case 7:
                    showProductDetailActvity();
                    break;
                case 8:
                    showPhotoPublishActivity();
                    break;
                // 第四行
                case 9:
                    showProductEditActvity();
                    break;
                case 10:
                    imageCompress();
                    break;
                case 11:
                    photoview();
                    break;
            }
        }
    }
}