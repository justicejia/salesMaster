package com.sohu.focus.salesmaster.kernal.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.imageloader.ImageLoaderBuilder;
import com.sohu.focus.salesmaster.kernal.imageloader.glide.debug.LoggingListener;
import com.sohu.focus.salesmaster.kernal.imageloader.glide.debug.NoOpRequestListener;
import com.sohu.focus.salesmaster.kernal.log.Logger;


/**
 * Created by qiangzhao on 2016/11/10.
 * <p>
 * diskCacheStrategy :  DiskCacheStrategy.NONE 什么都不缓存
 * DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。
 * DiskCacheStrategy.RESULT 仅仅缓存最终的图像，即，降低分辨率后的（或者是转换后的）（默认行为）
 * DiskCacheStrategy.ALL 缓存所有版本的图像
 */

public class GlideImageLoader extends ImageLoaderBuilder {

    RequestManager requestManager;

    public GlideImageLoader(Context context) {
        if (context == null) {
            requestManager = Glide.with(BaseApplication.getApplication());
        } else {
            try {
                requestManager = Glide.with(context);
            } catch (Exception e) {
                requestManager = Glide.with(BaseApplication.getApplication());
                Logger.ZQ().e("load with context failed   :" + e.getMessage());
            }
        }
    }

    @Override
    public void init(Context context) {
        super.init(context);
        if (context == null) {
            requestManager = Glide.with(BaseApplication.getApplication());
        } else {
            try {
                requestManager = Glide.with(context);
            } catch (Exception e) {
                requestManager = Glide.with(BaseApplication.getApplication());
                Logger.ZQ().e("load with context failed   :" + e.getMessage());
            }
        }
    }

    @Override
    public void clear() {
        Glide.get(BaseApplication.getApplication()).clearMemory();
    }

    @Override
    public void display() {
        if (imgFile != null) {
            displayImgByFile(requestManager);
            return;
        }

        if (imgUrl != null) {
            displayImgByUrl(requestManager);
            return;
        }
    }

    private void displayImgByUrl(RequestManager requestManager) {
        this.requestManager = requestManager;
        if (img.get() == null)
            return;
        DrawableRequestBuilder builder = requestManager
                .load(imgUrl)
                .skipMemoryCache(!needCache)
                .diskCacheStrategy(needCache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .placeholder(placeholderId)
                .error(errorId);
        if (width != 0 && height != 0)
            builder.override(width, height);
        switch (scale_type) {
            case FIT_CENTER:
                builder.fitCenter();
                break;
            case CENTER_CROP:
                builder.centerCrop();
                break;
            case FIT_XY:
                break;
        }
        if (!animate)
            builder.dontAnimate();
        builder.listener(showLog ? new LoggingListener() : new NoOpRequestListener())
                .into(img.get());
    }


    @Override
    public void asBitmap(final ImageLoadFinishListener listener) {
        requestManager.load(imgUrl)
                .asBitmap()
                .skipMemoryCache(!needCache)
                .diskCacheStrategy(needCache ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.NONE)
                .placeholder(placeholderId)
                .error(errorId)
                .listener(showLog ? new LoggingListener() : new NoOpRequestListener())
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (listener != null)
                            listener.onLoadFinish(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (listener != null)
                            listener.onLoadFinish(null);
                    }
                });
    }

    /**
     * 解决recyclerview中，holder加载同一个url造成task没有执行的bug
     *
     * @param view
     */
    @Override
    public void displayInto(final ImageView view) {
        requestManager.load(imgUrl)
                .asBitmap()
                .skipMemoryCache(!needCache)
                .diskCacheStrategy(needCache ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.NONE)
                .placeholder(placeholderId)
                .error(errorId)
                .listener(showLog ? new LoggingListener() : new NoOpRequestListener())
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (view != null && resource != null)
                            view.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    }
                });
    }

    private void displayImgByFile(RequestManager requestManager) {
        this.requestManager = requestManager;
        if (img.get() == null)
            return;
        DrawableRequestBuilder builder = requestManager
                .load(imgFile)
                .centerCrop()
                .diskCacheStrategy(cacheFile ? DiskCacheStrategy.RESULT : DiskCacheStrategy.NONE)
                .placeholder(placeholderId)
                .error(errorId);
        if (width != 0 && height != 0)
            builder.override(width, height);
        if (scale_type == SCALE_TYPE.FIT_CENTER)
            builder.fitCenter();
        else
            builder.centerCrop();
        if (!animate)
            builder.dontAnimate();
        builder.listener(showLog ? new LoggingListener() : new NoOpRequestListener())
                .into(img.get());
    }

    @Override
    public AbsListView.OnScrollListener getListPauseOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        ListPauseOnScrollListener listPauseOnScrollListener = new ListPauseOnScrollListener(requestManager, false, true, onScrollListener);
        return listPauseOnScrollListener;
    }


    @Override
    public RecyclerView.OnScrollListener getRecyclePauseOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        RecyclerPauseOnScrollListener recyclerPauseOnScrollListener = new RecyclerPauseOnScrollListener(requestManager, false, true, onScrollListener);
        return recyclerPauseOnScrollListener;
    }

}
