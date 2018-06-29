package com.sohu.focus.salesmaster.http;

import com.sohu.focus.salesmaster.kernal.http.BaseApi;

import rx.Observable;

/**
 * Created by yuanminjia on 2017/11/2.
 */

public abstract class FocusBaseApi extends BaseApi {
    @Override
    public Observable getObservable(Object methods) {
        if (methods instanceof HttpService)
            return getObservable((HttpService) methods);
        else
            return null;
    }

    public abstract Observable getObservable(HttpService methods);
}