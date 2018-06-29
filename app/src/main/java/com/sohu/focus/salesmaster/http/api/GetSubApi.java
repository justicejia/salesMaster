package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostUserId;

import rx.Observable;

/**
 * Created by yuanminjia on 2017/11/2.
 */

public class GetSubApi extends FocusBaseApi {

    private PostUserId param;

    public PostUserId getParam() {
        return param;
    }

    public void setParam(PostUserId param) {
        this.param = param;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        if (param == null) return null;
        return methods.getSubordinates(param);
    }
}
