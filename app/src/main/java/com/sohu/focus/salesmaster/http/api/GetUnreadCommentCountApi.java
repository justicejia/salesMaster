package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetUnreadCommentCount;

import rx.Observable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class GetUnreadCommentCountApi extends FocusBaseApi {
    private GetUnreadCommentCount model;

    public GetUnreadCommentCountApi(GetUnreadCommentCount model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUnreadCommentCount(model);
    }
}
