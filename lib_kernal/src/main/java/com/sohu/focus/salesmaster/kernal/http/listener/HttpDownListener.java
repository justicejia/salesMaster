package com.sohu.focus.salesmaster.kernal.http.listener;

/**
 * 下载过程中的回调处理
 * Created by qiangzhao on 2016/11/16.
 */

public abstract class HttpDownListener<T> {

    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 开始下载
     */
    public abstract void onStart();

    /**
     * 完成下载
     */
    public abstract void onComplete();

    /**
     * 下载进度
     * @param readLength
     * @param countLength
     */
    public abstract void updateProgress(long readLength, long countLength);

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
    public  void onError(Throwable e){

    }

    /**
     * 暂停下载
     */
    public void onPause(){

    }

    /**
     * 停止下载销毁
     */
    public void onStop(){

    }
}
