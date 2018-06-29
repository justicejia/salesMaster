package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;

import rx.Observable;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

public class GetFilterDataApi extends FocusBaseApi {
    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getFilterData();
    }
}
