package com.sohu.focus.salesmaster.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.sohu.focus.salesmaster.R;
import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.http.HttpDownloadEngine;
import com.sohu.focus.salesmaster.kernal.http.download.DownInfo;
import com.sohu.focus.salesmaster.kernal.http.download.DownState;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpDownListener;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;
import com.sohu.focus.salesmaster.kernal.utils.StorageUtil;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import java.io.File;

import static android.os.Build.VERSION_CODES.N;


/**
 * 更新并下载安装包
 * Created by yuanminjia on 17-12-13.
 */

public enum UpgradeUtil {

    INSTANCE;

    public static final int NOTIFICATION_ID = 1234;
    public boolean downloading = false, downloaded = false;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    String apkName = "FocusUpdateApk.apk";

    UpgradeUtil() {
        mNotifyManager = (NotificationManager) BaseApplication.getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(BaseApplication.getApplication());
        mBuilder.setContentTitle("下载焦点效率新版应用").setContentText("正在下载").setSmallIcon(R.mipmap
                .ic_launcher);
    }

    public void startDownloadApk(String apkUrl) {
        ToastUtil.toast("正在为您准备新版应用，请稍候");
        downloading = true;
        File output = new File(SalesFileUtil.getDownLoadPath(), apkName);
        DownInfo downInfo = new DownInfo(apkUrl);
        downInfo.setState(DownState.START);
        downInfo.setSavePath(output.getAbsolutePath());
        downInfo.setConnectonTime(NetUtil.TIMEOUT_TIME);
        downInfo.setRange(false);
        downInfo.setListener(new ApkDownloadListener());
        HttpDownloadEngine httpDownloadEngine = HttpDownloadEngine.getInstance();
        httpDownloadEngine.startDown(downInfo);
    }

    public void installNewApk(File file) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW);
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= N) {
            promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        promptInstall.setDataAndType(StorageUtil.getUri(BaseApplication.getApplication(), file), "application/vnd.android.package-archive");
        BaseApplication.getApplication().startActivity(promptInstall);
    }

    private class ApkDownloadListener extends HttpDownListener<DownInfo> {

        @Override
        public void onNext(DownInfo baseDownEntity) {
            downloading = false;
            downloaded = true;
            mBuilder.setContentText("下载完毕～")
                    .setProgress(0, 0, false);
            mBuilder.setAutoCancel(true);
            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
            mNotifyManager.cancel(NOTIFICATION_ID);
            if (!TextUtils.isEmpty(baseDownEntity.getSavePath())) {
                installNewApk(new File(baseDownEntity.getSavePath()));
            }
        }

        @Override
        public void onStart() {
            downloading = true;
        }

        @Override
        public void onComplete() {
            downloading = false;
            mBuilder.setContentText("下载完毕～")
                    .setProgress(0, 0, false);
            mBuilder.setAutoCancel(true);
            mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
        }

        @Override
        public void onError(Throwable e) {
            downloading = false;
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            int progress = (int) ((float) readLength / countLength * 100);
            if (progress % 5 == 0) {
                mBuilder.setProgress(100, progress, false);
                mBuilder.setContentText("正在下载 " + progress + "%");
                mNotifyManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }
}