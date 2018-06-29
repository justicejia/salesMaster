package com.sohu.focus.salesmaster.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.sohu.focus.salesmaster.base.SalesConstants;

/**
 * 权限类,动态权限申请主要是相机和存储权限
 * Created by yuanminjia on 2017/11/3.
 */

public class SalesPermissionUtil {

    /**
     * 申请相机权限
     */
    public static void requestCameraPermission(final Activity activity, OnCameraPermissionListener listener) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                showCameraDialog(activity);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, SalesConstants.REQUEST_PERMISSION_CAMERA);
            }
        } else {
            if (listener != null) {
                listener.onCameraPermissionSuccess();
            }
        }
    }

    /**
     * 申请存储权限
     */
    public static void requestStoragePermission(final Activity activity, OnStoragePermissionListener listener) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showStorageDialog(activity);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SalesConstants.REQUEST_PERMISSION_STORAGE);
            }
        } else {
            if (listener != null) {
                listener.onStoragePermissionSuccess();
            }
        }
    }


    private static void showCameraDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("申请相机权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA}, SalesConstants.REQUEST_PERMISSION_CAMERA);
                    }
                })
                .show();
    }

    private static void showStorageDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage("申请读写权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SalesConstants.REQUEST_PERMISSION_STORAGE);
                    }
                })
                .show();
    }

    public static boolean hasStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    public interface OnCameraPermissionListener {
        void onCameraPermissionSuccess();
    }

    public interface OnStoragePermissionListener {
        void onStoragePermissionSuccess();
    }


}
