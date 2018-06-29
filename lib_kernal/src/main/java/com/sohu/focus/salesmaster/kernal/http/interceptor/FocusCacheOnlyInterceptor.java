package com.sohu.focus.salesmaster.kernal.http.interceptor;


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

public class FocusCacheOnlyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        Response response = chain.proceed(request);
        int maxStale = NetUtil.CACHE_NO_NETWORK_TIME; // 没网失效1天
        Response responseLatest= response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .build();
        return responseLatest;
    }

}
