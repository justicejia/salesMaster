package com.sohu.focus.salesmaster.kernal.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhaoqiang on 2017/8/14.
 */

public class FocusTagInterceptor implements Interceptor {

    private String tag;

    public FocusTagInterceptor(String tag) {
        this.tag = tag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (request != null) {
            Request.Builder signedRequestBuilder = request.newBuilder();
            signedRequestBuilder.tag(tag);
            request = signedRequestBuilder.build();
            request.tag();
        }
        return chain.proceed(request);
    }
}
