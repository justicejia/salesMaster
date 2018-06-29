package com.sohu.focus.salesmaster.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sohu.focus.salesmaster.kernal.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.DEFAULT_DOWNLOAD_DIR;
import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.DEFAULT_EXTERNAL_IMAGE_DIR;

/**
 * 文件存储
 * <p>
 * CacheDir --- sdcard/Android/Cache
 * 相册缓存文件位于      CacheDir --- com.sohu.focus.salesmaster/cache／img/camera/      文件夹下
 * 转换用的png图片位于   CacheDir --- com.sohu.focus.salesmaster/cache／img/下
 * 下载楼盘文件位于      sdcard --- com.sohu.salesmaster/download 下
 * </p>
 * Created by yuanminjia on 2017/11/3.
 */

public class SalesFileUtil {

    /**
     * 楼盘文件保存位置
     */
    public static final String EXTERNAL_DOWNLOAD_DIR = "com.sohu.salesmaster/download";

    public static File getCacheDir() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)
                && BaseApplication.getApplication().getExternalCacheDir() != null
                && BaseApplication.getApplication().getExternalCacheDir().exists())
            return BaseApplication.getApplication().getExternalCacheDir();
        else
            return BaseApplication.getApplication().getCacheDir();
    }

    /**
     * 产生图片文件
     *
     * @param paramString 根目录下的子文件名
     */
    private static File generateImgFile(String paramString) {
        File imgCacheDir = new File(getCacheDir(), DEFAULT_EXTERNAL_IMAGE_DIR);
        if (!imgCacheDir.exists()) {
            imgCacheDir.mkdirs();
        }
        String pathPrefix = imgCacheDir.getAbsolutePath() + "/";
        String str = pathPrefix + paramString;
        return new File(str);
    }

    /**
     * @return 获取相机图片的路径目录文件
     */
    public static File getCameraFile() {
        File cameraPath = generateImgFile("camera");
        if (!cameraPath.exists())
            cameraPath.mkdirs();
        return cameraPath;
    }

    public static String getDownLoadPath() {
        File downloadFile = new File(getCacheDir(), DEFAULT_DOWNLOAD_DIR);
        return downloadFile.getAbsolutePath();
    }

    /**
     * png转jpg
     *
     * @param pngPath png图片文件的绝对路径
     * @return 转换后的jpg图片
     */
    public static File convertPNG2JPG(String pngPath) {
        File jpg = generateImgFile("sales" + System.currentTimeMillis() + ".jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(pngPath);
        try {
            FileOutputStream out = new FileOutputStream(jpg);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpg;
    }

}
