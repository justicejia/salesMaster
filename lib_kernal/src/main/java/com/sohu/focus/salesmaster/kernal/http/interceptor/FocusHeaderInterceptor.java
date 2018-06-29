package com.sohu.focus.salesmaster.kernal.http.interceptor;


import com.sohu.focus.salesmaster.kernal.utils.NetUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * header拦截器
 * Created by qiangzhao on 2016/10/26.
 */

public class FocusHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .removeHeader("User-Agent")
                .header("User-Agent", NetUtil.getSalesUserAgent(true))
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }

}
