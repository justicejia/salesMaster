package com.sohu.focus.salesmaster.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.sohu.focus.salesmaster.kernal.BaseApplication;

import java.io.File;

/**
 * 系统自带的下载方法
 * Created by yuanminjia on 2018/1/31.
 */

public enum DownLoadUtil {

    INSTANCE;

    private DownloadManager.Request mRequest;
    private long downloadId;

    /**
     * @param downloadUrl 下载链接
     * @param fileName    下载文件名字
     * @param saveFile    下载后的文件名
     */
    public void prepare(String fileName, String downloadUrl, File saveFile) {
        mRequest = new DownloadManager.Request(Uri.parse(downloadUrl));
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        mRequest.setTitle("正在下载");
        mRequest.setDescription(fileName);
        mRequest.setDestinationUri(Uri.fromFile(saveFile));
    }


    public void startDownLoad() {
        DownloadManager manager = (DownloadManager) BaseApplication.getApplication().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            downloadId = manager.enqueue(mRequest);
        }
    }
}
