package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory2;

import rx.Observable;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

public class GetUserKpiHistoryApi2 extends FocusBaseApi {

    private GetUsrKpiHistory2 model;

    public void setModel(GetUsrKpiHistory2 model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUserKpiHistory2(model);
    }
}
