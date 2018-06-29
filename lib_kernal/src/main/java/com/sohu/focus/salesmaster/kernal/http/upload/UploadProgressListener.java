package com.sohu.focus.salesmaster.kernal.http.upload;

/**
 * Created by qiangzhao on 2016/12/14.
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}
