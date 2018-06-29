package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetProjsByUid;

import rx.Observable;

/**
 * Created by luckyzhangx on 2017/11/2.
 */

public class GetProjsByUidApi extends FocusBaseApi {

    private static final String TAG = "GetProjsByUidApi";

    GetProjsByUid param;

    public GetProjsByUidApi() {
        setCache(true);
    }

    public GetProjsByUid getParam() {
        return param;
    }

    public void setParam(GetProjsByUid param) {
        this.param = param;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getProjsByUid(param);
    }
}
