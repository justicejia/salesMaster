package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.DelComment;

import rx.Observable;

/**
 * Created by luckyzhangx on 16/03/2018.
 */

public class DelCommentApi extends FocusBaseApi {
    private DelComment model;

    public DelCommentApi(DelComment model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.delComment(model);
    }
}
