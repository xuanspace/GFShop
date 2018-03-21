package com.topway.fine.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.cache.disk.FileCache;
import com.facebook.common.disk.NoOpDiskTrimmableRegistry;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.ByteConstants;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import static com.topway.fine.utils.NetUtil.uri;

/**
 * Created by lwx on 2017/4/21.
 */

public class FrescoUtil {

    //分配的可用内存
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    //使用的缓存数量
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    //小图极低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_VERYLOW_CACHE_SIZE = 60 * ByteConstants.MB;

    //小图低磁盘空间缓存的最大值（特性：可将大量的小图放到额外放在另一个磁盘空间防止大图占用磁盘空间而删除了大量的小图）
    private static final int MAX_SMALL_DISK_LOW_CACHE_SIZE = 60 * ByteConstants.MB;

    //默认图极低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_VERYLOW_SIZE = 60 * ByteConstants.MB;

    //默认图低磁盘空间缓存的最大值
    private static final int MAX_DISK_CACHE_LOW_SIZE = 60 * ByteConstants.MB;

    //默认图磁盘缓存的最大值
    private static final int MAX_DISK_CACHE_SIZE = 100 * ByteConstants.MB;

    //小图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_SMALL_CACHE_DIR = "ImagePipelineCacheSmall";

    //默认图所放路径的文件夹名
    private static final String IMAGE_PIPELINE_CACHE_DIR = "ImagePipelineCacheDefault";

    public Context context;
    public String CACHE_DIRECTORY;

    /**
     * 初始化Fresco缓存设置
     * @param context
     */
    public void init(Context context) {

    }


    public static ImagePipelineConfig getDefaultImagePipelineConfig(Context context) {

        // 内存配置
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE,  // 内存缓存中总图片的最大大小,以字节为单位
                Integer.MAX_VALUE,      // 内存缓存中图片的最大数量
                MAX_MEMORY_CACHE_SIZE,  // 内存缓存中准备清除但尚未被删除的总图片的最大大小,以字节为单位
                Integer.MAX_VALUE,      // 内存缓存中准备清除的总图片的最大数量
                Integer.MAX_VALUE);     // 内存缓存中单个图片的最大大小

        // 修改内存图片缓存数量，空间策略
        Supplier<MemoryCacheParams> bitmapCacheParamsSupplier = new Supplier<MemoryCacheParams>() {
            @Override
            public MemoryCacheParams get() {
                return bitmapCacheParams;
            }
        };

        // 小图片的磁盘配置
        DiskCacheConfig smallImageDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir()) //缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR) //文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE) //默认缓存的最大大小
                .setMaxCacheSizeOnLowDiskSpace(MAX_SMALL_DISK_LOW_CACHE_SIZE) //缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_SMALL_DISK_VERYLOW_CACHE_SIZE) //缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        // 默认图片的磁盘配置
        DiskCacheConfig mainDiskCacheConfig = DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPath(Environment.getExternalStorageDirectory().getAbsoluteFile())//缓存图片基路径
                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)//文件夹名
                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)//默认缓存的最大大小。
                .setMaxCacheSizeOnLowDiskSpace(MAX_DISK_CACHE_LOW_SIZE)//缓存的最大大小,使用设备时低磁盘空间。
                .setMaxCacheSizeOnVeryLowDiskSpace(MAX_DISK_CACHE_VERYLOW_SIZE)//缓存的最大大小,当设备极低磁盘空间
                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
                .build();

        // 缓存图片配置
        ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context)
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .setBitmapMemoryCacheParamsSupplier(bitmapCacheParamsSupplier)
                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                .setResizeAndRotateEnabledForNetwork(true);

        // 当内存紧张时清理缓存
        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                //Loger.d(String.format("onCreate suggestedTrimRatio : %d", suggestedTrimRatio));
                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
                }
            }
        });

        return configBuilder.build();
    }

    /**
     * 获取fresco本地缓存图片的文件
     * @param uri 请求路径
     * @return 缓存文件
     */
    public File getCacheFile(Uri uri) {
        FileCache fileCache = Fresco.getImagePipelineFactory().getMainFileCache();
        FileBinaryResource resource = (FileBinaryResource) fileCache.getResource(new SimpleCacheKey(uri.toString()));
        return resource.getFile();
    }

    public boolean isCache(String url) {
        return false;
    }

    private void downLoadImg(Uri uri) {
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(@Nullable Bitmap bitmap) {
                //bitmap即为下载所得图片
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {

            }
        }, CallerThreadExecutor.getInstance());

        Resources resources = null;
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                .setFadeDuration(300)
                //                .setPlaceholderImage(defaultDrawable)
                //                .setFailureImage(defaultDrawable)
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        DraweeHolder<GenericDraweeHierarchy> draweeHolder = DraweeHolder.create(hierarchy, context);

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(imageRequest)
                .build();
        controller.onClick();
    }
}
