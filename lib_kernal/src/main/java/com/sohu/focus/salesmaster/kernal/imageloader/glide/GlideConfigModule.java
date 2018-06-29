package com.sohu.focus.salesmaster.kernal.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.sohu.focus.salesmaster.kernal.utils.ImageUtils;


/**
 * Created by qiangzhao on 2016/11/10.
 */

public class GlideConfigModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 指定位置在packageName/cache/image_cache,大小为MAX_CACHE_DISK_SIZE的磁盘缓存
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, ImageUtils.DISK_CACHE_PATH, ImageUtils.MAX_CACHE_DISK_SIZE));
        //指定内存缓存大小
        builder.setMemoryCache(new LruResourceCache(ImageUtils.MAX_CACHE_MEMORY_SIZE));
        //全部的内存缓存用来作为图片缓存
        builder.setBitmapPool(new LruBitmapPool(ImageUtils.MAX_CACHE_MEMORY_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
//        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
