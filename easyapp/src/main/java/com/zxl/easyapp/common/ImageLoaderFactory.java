package com.zxl.easyapp.common;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by 张晓莉 on 2016/8/12.
 * 使用ImageLoader图片加载
 */
public class ImageLoaderFactory {
    private static final int CACHE_SIZE = 128 * 1024 * 1024;
    private static final int TIME_OUT = 60 * 1000;
    private static ImageLoader imageLoader;
    private static ImageLoaderConfiguration imageLoaderConfiguration;
    private static Context context;

    public static void init(Context mContext, int imageRes) {
        context = mContext;
        initImageLoader(imageRes);
    }

    private static void initImageLoader(int imageRes) {
        if (imageLoader == null) {
            imageLoader = ImageLoader.getInstance();
            initImageLoaderConfiguration(imageRes);
            imageLoader.init(imageLoaderConfiguration);
        }
    }

    private static void initImageLoaderConfiguration(int imageRes) {
        if (imageLoaderConfiguration == null) {
            DisplayImageOptions defaultOptions = generateDefaultDisplayImageOptions(imageRes);
            imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(3)
                    .memoryCacheSize(CACHE_SIZE)
                    .memoryCache(new WeakMemoryCache())
                    .diskCacheSize(CACHE_SIZE)
                    .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                    .imageDownloader(new BaseImageDownloader(context, TIME_OUT, TIME_OUT))
                    .defaultDisplayImageOptions(defaultOptions)
                    .build();
        }
    }

    public static DisplayImageOptions generateDefaultDisplayImageOptions(int imageRes) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .resetViewBeforeLoading(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(imageRes)
                .showImageForEmptyUri(imageRes)
                .showImageOnFail(imageRes)
                .build();
        return defaultOptions;
    }

    public static DisplayImageOptions generateNoOnLoadingDisplayImageOptions(int imageRes) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .resetViewBeforeLoading(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(imageRes)
                .showImageOnFail(imageRes)
                .build();
        return defaultOptions;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }
}