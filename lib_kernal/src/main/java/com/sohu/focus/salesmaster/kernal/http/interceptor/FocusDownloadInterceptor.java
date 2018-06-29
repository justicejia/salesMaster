package com.sohu.focus.salesmaster.kernal.http.interceptor;


import com.sohu.focus.salesmaster.kernal.http.download.FocusDownloadResponseBody;
import com.sohu.focus.salesmaster.kernal.http.listener.DownloadProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by qiangzhao on 2016/11/15.
 */

public class FocusDownloadInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public FocusDownloadInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new FocusDownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
