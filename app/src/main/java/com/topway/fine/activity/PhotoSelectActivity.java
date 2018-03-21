package com.topway.fine.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.topway.fine.R;
import com.topway.fine.adapter.PhotoFolderAdapter;
import com.topway.fine.adapter.PhotoPublishAdapter;
import com.topway.fine.adapter.PhotoSelectAdapter;
import com.topway.fine.app.APP;
import com.topway.fine.base.BaseActivity;
import com.topway.fine.model.AlbumFolder;
import com.topway.fine.model.AlbumPhoto;
import com.topway.fine.utils.AlbumUtil;
import com.topway.fine.utils.DeviceUtil;
import com.topway.fine.utils.FileUtils;
import com.topway.fine.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片选择器
 * @version 1.0
 * @created 2016-3-1
 * @aouthor linweixuan@gmial.com
 */
public class PhotoSelectActivity extends BaseActivity {

    private static final String TAG = PhotoSelectActivity.class.getSimpleName();
    private static final String EXTRA_DATA = "extra_data";
    private static final String EXTRA_INDEX = "extra_index";

    @Bind(R.id.gv_photo) GridView gridView;
    @Bind(R.id.btn_category) Button category;
    @Bind(R.id.catalog_mask_view) View maskView;
    @Bind(R.id.footer) View popupAnchorView;
    @Bind(R.id.preview) Button preview;
    @Bind(R.id.iv_back) ImageView back;
    @Bind(R.id.tv_confirm) TextView confirm;


    private PhotoSelectActivity context;
    private PhotoSelectAdapter photoAdapter;
    private PhotoFolderAdapter folderAdapter;
    private PhotoSelectAdapter.OnItemCheckListener listener;
    private ListPopupWindow popupWindow;
    private List<AlbumFolder> albums;
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        ButterKnife.bind(this);
        initActionBar();
        initGridView();
        initImageLoader();
        initImageCategory();
        initAlbumPhotos();
    }

    // 初始化标题栏
    private void initActionBar() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOkResult(photoAdapter.getSelectedImages());
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoPreviewActivity();
            }
        });

    }

    // 初始化GridView
    private void initGridView() {
        folderAdapter = new PhotoFolderAdapter(context);
        photoAdapter = new PhotoSelectAdapter(context);
        photoAdapter.setOnItemCheckListener(new GridItemCheckListener());

        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new GridViewListener());
        gridView.setAdapter(photoAdapter);
    }

    // 初始化图片加载器
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3) // default 3
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(3 * 1024 * 1024))
                .memoryCacheSize(3 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).defaultDisplayImageOptions(new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.default_error)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                        .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build())
                .build();
        ImageLoader.getInstance().init(config);
    }

    // 获取相册所有图片
    public void initAlbumPhotos() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                folderAdapter.setData(albums);
                photoAdapter.setData(AlbumUtil.getAllPhotos(albums));
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message message = Message.obtain();
                albums = AlbumUtil.getAlbums(context);
                message.obj = albums;
                handler.sendMessage(message);
            }
        }.start();
    }

    // 初始化图片目录选择按钮
    private void initImageCategory() {
        category.setText("所有图片");
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow == null) {
                    createPopupFolderList();
                }

                if (popupWindow.isShowing()) {
                    maskView.setVisibility(View.INVISIBLE);
                    maskView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_to_zero));
                    popupWindow.dismiss();
                } else {
                    maskView.setVisibility(View.VISIBLE);
                    maskView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_to_one));
                    popupWindow.show();

                    //监听大小，如果界面过大则调小
                    popupWindow.getListView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onGlobalLayout() {
                            int height = popupWindow.getListView().getHeight();
                            int scrHeight = DeviceUtil.getHeight(context);
                            int limiteHeigt = height + 4 * getActionBarHeight(context);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                popupWindow.getListView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                popupWindow.getListView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                            if (limiteHeigt > scrHeight) {
                                popupWindow.setHeight(5 * scrHeight / 8);
                                popupWindow.getListView().getLayoutParams().height = 5 * scrHeight / 8;
                                popupWindow.show();
                            }
                        }
                    });

                    int index = folderAdapter.getSelectIndex();
                    index = (index == 0) ? index : index - 1;
                    popupWindow.getListView().setSelection(index);
                }
            }
        });
    }

    // 获取标题栏高度
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return 0;
    }

    // 创建相册文件夹列表
    private void createPopupFolderList() {
        int width = DeviceUtil.getWidth(context);
        popupWindow = new ListPopupWindow(context);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAdapter(folderAdapter);
        popupWindow.setContentWidth(width);
        popupWindow.setWidth(width);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnchorView(popupAnchorView);
        popupWindow.setModal(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss() {
                maskView.setVisibility(View.INVISIBLE);
                maskView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_to_zero));
            }
        });

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                folderAdapter.setSelectIndex(position);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow.dismiss();
                        if (position == 0) {
                            category.setText("所有图片");
                            photoAdapter.setData(AlbumUtil.getAllPhotos(albums));
                        }
                        else {
                            AlbumFolder folder = (AlbumFolder) adapterView.getItemAtPosition(position);
                            if (null != folder) {
                                photoAdapter.setData(AlbumUtil.getAlbumPhotos(albums, folder.getName()));
                                category.setText(folder.getName());
                            }
                        }
                        // 滑动到最初始位置
                        gridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }

    // 图片点击事件
    private class GridViewListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                showCameraAction();
            } else {
                AlbumPhoto photo = (AlbumPhoto) parent.getItemAtPosition(position);
            }
        }
    }

    // 图片选择事件
    private class GridItemCheckListener implements PhotoSelectAdapter.OnItemCheckListener {
        @Override
        public void onItemCheck(int curSelectedCount, int total, int position) {
            preview.setEnabled(curSelectedCount > 0);
            preview.setClickable(curSelectedCount > 0);
            preview.setText(String.format(getString(R.string.preview_format), curSelectedCount));

            confirm.setEnabled(curSelectedCount > 0);
            if (curSelectedCount != 0) {
                confirm.setText(String.format(getString(R.string.confirm_format), curSelectedCount, total));
            } else {
                confirm.setText(getString(R.string.confirm));
            }
        }
    }

    // 调用相机拍照
    private void showCameraAction() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径,创建临时文件
            String path = AlbumUtil.getCameraPath(albums);
            if (!StringUtils.isEmpty(path)) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
                String fileName = "multi_image_" + timeStamp + "";
                tempFile = new File(path, fileName + ".jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(cameraIntent, APP.REQUEST_CAMERA);
            }
        } else {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    // 显示图片预览
    public void showPhotoPreviewActivity(File file) {
        if (file != null) {
            String path = file.getAbsolutePath();
            ArrayList<String> previewImages = new ArrayList<String>();
            previewImages.add(path);
            Intent intent = new Intent(this, PhotoPreviewActivity.class);
            intent.putExtra(EXTRA_DATA, previewImages);
            intent.putExtra(EXTRA_INDEX, 0);
            this.startActivityForResult(intent, APP.REQUEST_PHOTO_PREVIEW);
        }
    }

    // 显示图片预览
    public void showPhotoPreviewActivity() {
        ArrayList<String> previewImages = new ArrayList<String>();
        for (AlbumPhoto photo : photoAdapter.getSelectedImages()) {
            previewImages.add(photo.getPath());
        }
        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_DATA, previewImages);
        intent.putExtra(EXTRA_INDEX, 0);
        this.startActivityForResult(intent, APP.REQUEST_PHOTO_PREVIEW);
    }

    // 设置图片选择结果集
    private void setOkResult(ArrayList<AlbumPhoto> datas) {
        ArrayList<String> pathDataList = new ArrayList<>();
        for (AlbumPhoto photo : datas) {
            pathDataList.add(photo.getPath());
        }
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_DATA, pathDataList));
        finish();
    }

    // 获取相机拍摄图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 相机拍照完成后，返回图片路径
        if (requestCode == APP.REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    showPhotoPreviewActivity(tempFile);
                }
            }
            else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }

        // 预览选择的图片
        else if (requestCode == APP.REQUEST_PHOTO_PREVIEW) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }

}