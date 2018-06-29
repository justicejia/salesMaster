package com.sohu.focus.salesmaster.newFilter.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostUserId;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/6/8
 * description:
 */
public class HomeSheetFilterApi extends FocusBaseApi {

    private PostUserId id;

    public HomeSheetFilterApi(String uid) {
        id = new PostUserId();
        id.setUid(uid);
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getHomeSheetFilter(id);
    }
}
