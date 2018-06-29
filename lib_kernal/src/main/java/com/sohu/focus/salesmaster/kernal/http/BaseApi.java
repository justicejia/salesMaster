package com.sohu.focus.salesmaster.kernal.http;


import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;

import rx.Observable;

/**
 * Created by qiangzhao on 2016/11/14.
 */

public class BaseApi {
    /*是否需要缓存处理*/
    private boolean cache;
    /*基础url*/
    public static String BASE_URL = EnvironmentManager.getSalesBaseUrl();
    /*方法-如果需要缓存必须设置这个参数；不需要不用設置*/
    private String apiUrl;
    /*是否使用logging拦截器*/
    private boolean showLog = FocusLog.isDebugging;
    /*每个请求相应tag，可以根据tag取消请求*/
    private String tag = "FOCUS";
    /*自定义的base url，除非必要，不要设置*/
    private String customBaseUrl;

    private boolean isSkipErrToast = false;
    /*是否强制使用http请求，true 不会请求cacheOnly缓存*/
    private boolean isForceHttp = false;

    protected StringBuilder cacheKey = new StringBuilder();

    public BaseApi() {
    }

    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public Observable getObservable(Object methods) {
        return null;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public void setBaseUrl(String baseUrl) {
        this.BASE_URL = baseUrl;
    }

    public String getUrl() {
        return BASE_URL + apiUrl;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCustomBaseUrl() {
        return customBaseUrl;
    }

    public String getCompleteUrl() {
        return (CommonUtils.notEmpty(getCustomBaseUrl()) ? getCustomBaseUrl() : getBaseUrl()) + getApiUrl();
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.customBaseUrl = customBaseUrl;
    }

    public boolean isSkipErrToast() {
        return isSkipErrToast;
    }

    public void setSkipErrToast(boolean skipErrToast) {
        isSkipErrToast = skipErrToast;
    }

    public boolean isForceHttp() {
        return isForceHttp;
    }

    public void setForceHttp(boolean forceHttp) {
        isForceHttp = forceHttp;
    }

    public void generateCacheKey() {
        setApiUrl(cacheKey.toString());
    }

}
