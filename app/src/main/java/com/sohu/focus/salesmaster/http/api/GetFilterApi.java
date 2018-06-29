package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/5/21
 * description:
 */
public class GetFilterApi extends FocusBaseApi {
    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getFilterModel();
    }
}
