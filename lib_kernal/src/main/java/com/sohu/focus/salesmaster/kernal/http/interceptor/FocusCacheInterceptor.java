package com.sohu.focus.salesmaster.kernal.http.interceptor;



import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.utils.NetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;



/**
 * get缓存方式拦截器
 * Created by qiangzhao on 2016/10/26.
 */

public class FocusCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!NetUtil.isNetworkAvailable(BaseApplication.getApplication())) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        Response responseLatest;
        if (NetUtil.isNetworkAvailable(BaseApplication.getApplication())) {
            int maxAge = NetUtil.CACHE_HAS_NETWORK_TIME; //有网失效一分钟
            responseLatest = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = NetUtil.CACHE_NO_NETWORK_TIME; // 没网失效1天
            responseLatest= response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return  responseLatest;
    }

}
