package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetUsrKpiHistory;

import rx.Observable;

/**
 * Created by luckyzhangx on 05/02/2018.
 */

public class GetUserKpiHistoryApi extends FocusBaseApi {

    private GetUsrKpiHistory model;

    public void setModel(GetUsrKpiHistory model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUserKpiHistory(model);
    }
}
