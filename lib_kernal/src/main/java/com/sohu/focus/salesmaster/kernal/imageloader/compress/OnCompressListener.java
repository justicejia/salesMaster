package com.sohu.focus.salesmaster.kernal.imageloader.compress;

import java.util.List;

/**
 * Created by qiangzhao on 2016/12/27.
 */

public abstract class OnCompressListener {

    /**
     * Fired when the compression is started, override to handle in your own code
     */
    public void onStart(){}

    /**
     * Fired when a compression returns successfully, override to handle in your own code
     */
    public void onSuccess(CompressFileWrapper file){}

    /**
     * Fired when a compression fails to complete, override to handle in your own code
     */
    public abstract void onError(Throwable e);
    /**
     * Fired when compressions returns successfully, override to handle in your own code
     */
    public void onSuccess(List<CompressFileWrapper> files){}
}
