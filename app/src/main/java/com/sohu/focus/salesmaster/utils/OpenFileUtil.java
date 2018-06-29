package com.sohu.focus.salesmaster.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import java.io.File;
import java.util.Locale;


/**
 * 打开文件
 * Created by yuanminjia on 2018/1/31.
 */
public class OpenFileUtil {

    public static void openFile(String filePath, Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            ToastUtil.toast("文件不存在或已经删除");
            return;
        }
        /* 扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        if (end.equals("3gp") || end.equals("mp4")) {
            openVideo(filePath, context);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            openImage(filePath, context);
        } else if (end.equals("apk")) {
            openApk(filePath, context);
        } else if (end.equals("ppt") || end.equals("pptx")) {
            openPPT(filePath, context);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            openExcel(filePath, context);
        } else if (end.equals("doc") || end.equals("docx")) {
            openWord(filePath, context);
        } else if (end.equals("pdf")) {
            openPDF(filePath, context);
        } else if (end.equals("txt")) {
            openText(filePath, context);
        } else {
            openCommon(filePath, context);
        }

    }

    // Android获取一个用于打开APK文件的intent
    private static void openCommon(String filePath, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "*/*");
        context.startActivity(intent);
    }

    // Android获取一个用于打开APK文件的intent
    private static void openApk(String filePath, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    // Android获取一个用于打开VIDEO文件的intent
    private static void openVideo(String filePath, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "video/*");
        context.startActivity(intent);
    }


    // Android获取一个用于打开图片文件的intent
    private static void openImage(String filePath, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "image/*");
        context.startActivity(intent);

    }

    // Android获取一个用于打开PPT文件的intent
    private static void openPPT(String filePath, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        context.startActivity(intent);
    }

    // Android获取一个用于打开Excel文件的intent
    private static void openExcel(String filePath, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        context.startActivity(intent);
    }

    // Android获取一个用于打开Word文件的intent
    private static void openWord(String filePath, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/msword");
        context.startActivity(intent);
    }

    // Android获取一个用于打开文本文件的intent
    private static void openText(String filePath, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "text/plain");
        context.startActivity(intent);
    }

    // Android获取一个用于打开PDF文件的intent
    private static void openPDF(String filePath, Context context) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "com.sohu.focus.salesmaster.kernal.fileProvider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        intent.setDataAndType(uri, "application/pdf");
        context.startActivity(intent);
    }

}

