package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.base.SalesConstants;
import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.UpdateParams;


import rx.Observable;

/**
 * Created by yuanminjia on 2017/12/13.
 */

public class CheckUpdateApi extends FocusBaseApi {

    private UpdateParams params;

    public CheckUpdateApi(String appVersion){
        params = new UpdateParams();
        params.setAppId(SalesConstants.APP_ID);
        params.setAppType(2);
        params.setAppVersion(appVersion);
    }
    @Override
    public Observable getObservable(HttpService methods) {
        return methods.checkUpdate(params);
    }
}
