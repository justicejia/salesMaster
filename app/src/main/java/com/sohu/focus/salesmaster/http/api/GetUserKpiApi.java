package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetUserKpi;

import rx.Observable;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

public class GetUserKpiApi extends FocusBaseApi {
    private GetUserKpi model;

    public GetUserKpiApi(GetUserKpi model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUserKpiInfo(model);
    }
}
