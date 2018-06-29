package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetUnreadComment;

import rx.Observable;

/**
 * Created by luckyzhangx on 14/03/2018.
 */

public class GetUnreadCommentApi extends FocusBaseApi {
    private GetUnreadComment model;

    public GetUnreadCommentApi(GetUnreadComment model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUnreadComments(model);
    }
}
