package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.DelDynamics;

import rx.Observable;

/**
 * Created by luckyzhangx on 11/6/2017.
 */

public class DelDynamicApi extends FocusBaseApi {

    private DelDynamics delDynamics;

    public void setDelDynamics(DelDynamics delDynamics) {
        this.delDynamics = delDynamics;
    }

    @Override
    public Observable getObservable(HttpService methods) {

        return methods.delDynamic(delDynamics);
    }
}
