package com.sohu.focus.salesmaster.kernal.imageloader;

import android.content.Context;

import com.sohu.focus.salesmaster.kernal.imageloader.glide.GlideImageLoader;

/**
 * Created by qiangzhao on 2016/11/21.
 */

public class FocusImageLoader {

    private static volatile ImageLoaderBuilder sInstance;

    private FocusImageLoader() {}

    /**
     * 获取图片加载器
     *
     * @return
     */
    public static ImageLoaderBuilder getLoader(Context context) {
        if (sInstance == null) {
            synchronized (FocusImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new GlideImageLoader(context);
                }
            }
        }
        sInstance.init(context);
        return sInstance;
    }
}
