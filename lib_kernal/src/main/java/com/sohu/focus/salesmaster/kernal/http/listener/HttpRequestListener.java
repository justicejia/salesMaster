package com.sohu.focus.salesmaster.kernal.http.listener;

import com.sohu.focus.salesmaster.kernal.KernalConstants;

/**
 * Created by qiangzhao on 2016/11/14.
 */

public abstract class HttpRequestListener<T> {

    /**
     * code {@link KernalConstants#BUSINESS_STATUS_CODE_SUCCESS} 成功后回调方法
     * @param result
     * @param method
     */
    public abstract void onSuccess(T result, String method);

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
    public abstract void onError(Throwable e);

    /**
     * code 非{@link KernalConstants#BUSINESS_STATUS_CODE_SUCCESS} 返回，可以根据需求选择是否实现
     * @param result
     * @param method
     */
    public abstract void onFailed(T result, String method);
}
