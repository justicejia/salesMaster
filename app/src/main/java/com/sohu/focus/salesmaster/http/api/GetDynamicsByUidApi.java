package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByUid;

import rx.Observable;

/**
 * Created by luckyzhangx on 2017/11/3.
 */

public class GetDynamicsByUidApi extends FocusBaseApi {

    GetDynamicsByUid param;

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getDynamicsByUid(param);
    }

    public void setParam(GetDynamicsByUid param) {
        this.param = param;
    }
}
