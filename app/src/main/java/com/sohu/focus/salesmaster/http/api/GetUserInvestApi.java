package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostUserId;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/4/25
 * description:
 */
public class GetUserInvestApi extends FocusBaseApi {
    private String mId;

    public GetUserInvestApi(String uid) {
        mId = uid;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        PostUserId postUserId = new PostUserId();
        postUserId.setUid(mId);
        return methods.getUserInvestInfo(postUserId);
    }
}
