package com.sohu.focus.salesmaster.kernal.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.sohu.focus.salesmaster.kernal.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by qiangzhao on 2017/1/23.
 */

public class StorageUtil {

    public static final String DEFAULT_ALBUM_DIR = "file"; //相册的缓存目录

    public static final String DEFAULT_DISK_CACHE_DIR = "compress_cache"; //压缩后的图片目录

    public static final String DEFAULT_LOG_DIR = "logs"; //日志的缓存目录

    public static final String DEFAULT_NETWORK_CACHE_DIR = "retrofit_cache"; //http请求的缓存目录

    public static final String DEFAULT_DOWNLOAD_DIR = "download"; //下载的缓存目录

    public static final String DEFAULT_VIDEO_COVER_DIR = "cover"; //视频封面的缓存目录

    public static final String DEFAULT_EXTERNAL_IMAGE_DIR = "img"; //外部存储的图片缓存目录

    public static File getCacheDir() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED) && BaseApplication.getApplication().getExternalCacheDir() != null && BaseApplication.getApplication().getExternalCacheDir().exists())
            return BaseApplication.getApplication().getExternalCacheDir();
        else
            return BaseApplication.getApplication().getCacheDir();
    }

    public static Uri getUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal" + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static Uri getCameraUri(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /*获取文件真正地址，针对7.0系统*/
    public static File getRealFile(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (filePath.contains("/camera_photos/")) {
                filePath = filePath.replace("/camera_photos", getCachePrefix());
            }
            return new File(filePath);
        } else {
            return new File(filePath);
        }
    }

    private static String getCachePrefix() {
        if (CommonUtils.notEmpty(StorageUtil.getCacheDir().getAbsolutePath())) {
            String[] splits = StorageUtil.getCacheDir().getAbsolutePath().split("/Android/data");
            if (splits.length == 2)
                return splits[0];
        }
        return "";
    }

    public static File getExternalCacheDir() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/com.sohu.focus.live/";
        File cacheDir = new File(path);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    /**
     * 刷新媒体库
     */
    public static void updateMedia(final Context c, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(c, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    c.sendBroadcast(mediaScanIntent);
                }
            });
        } else {//4.4以下发送广播更新媒体库
            c.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(new File(file.getParent()))));
        }

    }

    /**
     * 保存bitmap到本地
     *
     * @param filePath 文件保存路径
     * @param fileName 图片名
     */
    public static boolean savePic(Bitmap bitmap, String filePath, String fileName) {
        if (bitmap == null) {
            return false;
        }
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            if (!fileDir.mkdirs()) {
                return false;
            }
        }
        File file = new File(fileDir, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            StorageUtil.updateMedia(BaseApplication.getApplication(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
