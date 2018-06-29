package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetDynamicDetail;

import rx.Observable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class GetDynamicDetailApi extends FocusBaseApi {
    GetDynamicDetail model;

    public GetDynamicDetailApi(GetDynamicDetail model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getDynamicDetail(model);
    }
}
