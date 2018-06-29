package com.sohu.focus.salesmaster.kernal.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.sohu.focus.salesmaster.kernal.imageloader.FocusImageLoader;
import com.sohu.focus.salesmaster.kernal.imageloader.ImageLoaderBuilder;
import com.sohu.focus.salesmaster.kernal.log.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by qiangzhao on 2016/11/10.
 */

public class ImageUtils {

    /*硬盘缓存路径*/
    public static final String DISK_CACHE_PATH = "image_cache";

    /**
     * 硬盘缓存空间50mb
     */
    public static final int MAX_CACHE_DISK_SIZE = 50 * 1024 * 1024;

    /**
     * 内存缓存空间为最大内存的1/8
     */
    public static final int MAX_CACHE_MEMORY_SIZE = (int) Runtime.getRuntime().maxMemory() / 8;

    /**
     * 圆角显示图片
     *
     * @param context  一般为activtiy
     * @param view     图片显示类
     * @param url      图片url
     * @param defResId 默认图 id
     */
    public static void showPicWithUrl(Context context, ImageView view, String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        try {
            if (TextUtils.isEmpty(url)) {
                view.setImageResource(defResId);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取网络类型
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blurBgPic(final Context context, final View view, final String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defResId);
            Drawable drawable = new BitmapDrawable(blurBitmap(bitmap, context.getApplicationContext(), 0.4f, 14));
            view.setBackground(drawable);
        } else {
            FocusImageLoader.getLoader(context).load(url)
                    .asBitmap(new ImageLoaderBuilder.ImageLoadFinishListener() {
                        @Override
                        public void onLoadFinish(Bitmap resource) {
                            if (resource == null) {
                                return;
                            }
                            final Bitmap bitmap = blurBitmap(resource, context.getApplicationContext(), 0.4f, 14);
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = new BitmapDrawable(bitmap);
                                    view.setBackground(drawable);
                                }
                            });
                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void blurBgPicContent(final Context context, final CollapsingToolbarLayout view, final String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defResId);
            Drawable drawable = new BitmapDrawable(blurBitmap(bitmap, context.getApplicationContext(), 0.4f, 14));
            view.setBackground(drawable);
        } else {
            FocusImageLoader.getLoader(context).load(url)
                    .asBitmap(new ImageLoaderBuilder.ImageLoadFinishListener() {
                        @Override
                        public void onLoadFinish(final Bitmap resource) {
                            if (resource == null) {
                                return;
                            }
                            rx.Observable.create(new rx.Observable.OnSubscribe<Bitmap>() {

                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                @Override
                                public void call(Subscriber<? super Bitmap> subscriber) {
                                    final Bitmap bitmap = blurBitmap(resource, context.getApplicationContext(), 0.4f, 14);
                                    subscriber.onNext(bitmap);
                                    subscriber.onCompleted();
                                }
                            })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<Bitmap>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(final Bitmap bitmap) {
                                            Drawable drawable = new BitmapDrawable(bitmap);
                                            view.setBackground(drawable);

                                            Drawable drawable2 = new BitmapDrawable(bitmap);
                                            view.setContentScrim(drawable2);
                                        }
                                    });
                        }
                    });
        }
    }

    public static Bitmap getBlurBitmap(final Context context, String url) {
        if (CommonUtils.isEmpty(url))
            return null;
        final Bitmap[] bitmap = new Bitmap[1];
        FocusImageLoader.getLoader(context).load(url)
                .asBitmap(new ImageLoaderBuilder.ImageLoadFinishListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onLoadFinish(Bitmap resource) {
                        if (resource == null) {
                            return;
                        }

                        bitmap[0] = blurBitmap(resource, context.getApplicationContext(), 0.4f, 14);
                    }
                });
        return bitmap[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap resource, Context context, float ratio, int radius) {
        if (resource == null) {
            Logger.ZQ().e("blur bmp is null");
            return null;
        }
        Matrix m = new Matrix();
        m.setScale(ratio, ratio);
        resource = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), m, false);
        Bitmap bitmap = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.drawBitmap(resource, 0, 0, paint);

        RenderScript rs = RenderScript.create(context.getApplicationContext());
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        blur.setInput(input);
        blur.setRadius(radius);
        blur.forEach(output);
        output.copyTo(bitmap);
        rs.destroy();

        if (!resource.isRecycled())
            resource.recycle();

        return bitmap;
    }

    // 根据原图绘制圆形图片
    static public Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (0 == min) {
            min = source.getHeight() > source.getWidth() ? source.getWidth() : source.getHeight();
        }
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(target);
        // 绘圆
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        // 设置交叉模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 根据图片url获取bitmap
     *
     * @param width  生成bitmap的宽度
     * @param height 高度
     */
    public static Bitmap getBitmapWithUrl(Context context, String url, int width, int height) {
        try {
            return Glide.with(context).load(url).asBitmap().centerCrop()
                    .into(width, height).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取圆角图片
     *
     * @param dp 圆角半径
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int dp) {
        if (bitmap == null) {
            return null;
        }
        float radius = Resources.getSystem().getDisplayMetrics().density * dp;
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }


    public static Bitmap getScreenShot(View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        view.buildDrawingCache(true);
        return view.getDrawingCache(true);
    }

}
