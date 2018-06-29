package com.sohu.focus.salesmaster.kernal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ImageView;


import com.sohu.focus.salesmaster.kernal.log.FocusLog;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by qiangzhao on 2016/11/21.
 * <p>
 * 图片加载抽象类，设置图片加载的基本信息
 */

public abstract class ImageLoaderBuilder {

    protected String imgUrl;
    protected File imgFile;
    protected Uri imgUri;
    protected int placeholderId;
    protected int errorId;
    protected boolean animate = true;
    protected WeakReference<ImageView> img;
    protected boolean showLog = FocusLog.isDebugging;
    protected SCALE_TYPE scale_type = SCALE_TYPE.CENTER_CROP;
    protected boolean cacheFile = false;
    protected int width, height;
    protected boolean needCache = true;

    public abstract void clear();

    public abstract void display();

    public abstract AbsListView.OnScrollListener getListPauseOnScrollListener(AbsListView.OnScrollListener onScrollListener);

    public abstract RecyclerView.OnScrollListener getRecyclePauseOnScrollListener(RecyclerView.OnScrollListener onScrollListener);

    public abstract void asBitmap(ImageLoadFinishListener listener);

    public abstract void displayInto(ImageView imageView);

    /**
     * 初始化，重新赋值
     */
    public void init(Context context) {
        imgUrl = "";
        imgFile = null;
        imgUri = null;
        placeholderId = 0;
        errorId = 0;
        img = null;
        animate = true;
        showLog = FocusLog.isDebugging;
        scale_type = SCALE_TYPE.CENTER_CROP;
        cacheFile = false;
        width = 0;
        height = 0;
        needCache = true;
    }

    public ImageLoaderBuilder load(String url) {
        this.imgUrl = url;
        return this;
    }

    public ImageLoaderBuilder load(File file) {
        this.imgFile = file;
        return this;
    }

    public ImageLoaderBuilder load(Uri uri) {
        this.imgUri = uri;
        return this;
    }

    public ImageLoaderBuilder placeholder(int drawableId) {
        this.placeholderId = drawableId;
        return this;
    }

    public ImageLoaderBuilder error(int drawableId) {
        this.errorId = drawableId;
        return this;
    }

    public ImageLoaderBuilder asCircle() {
        this.animate = false;
        return this;
    }

    public ImageLoaderBuilder into(ImageView view) {
        this.img = new WeakReference<>(view);
        return this;
    }

    public ImageLoaderBuilder showLog() {
        showLog = true;
        return this;
    }

    public ImageLoaderBuilder cacheFile() {
        cacheFile = true;
        return this;
    }

    public ImageLoaderBuilder fitCenter() {
        this.scale_type = SCALE_TYPE.FIT_CENTER;
        return this;
    }

    public ImageLoaderBuilder centerCrop() {
        this.scale_type = SCALE_TYPE.CENTER_CROP;
        return this;
    }

    public ImageLoaderBuilder fitXY() {
        this.scale_type = SCALE_TYPE.FIT_XY;
        return this;
    }

    public ImageLoaderBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageLoaderBuilder cache(boolean shouldCache) {
        this.needCache = shouldCache;
        return this;
    }

    public enum SCALE_TYPE {
        FIT_CENTER, CENTER_CROP, FIT_XY
    }

    public interface ImageLoadFinishListener {
        void onLoadFinish(Bitmap bitmap);
    }
}
