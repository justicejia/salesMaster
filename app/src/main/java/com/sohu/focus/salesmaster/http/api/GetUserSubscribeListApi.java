package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostUserId;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/5/29
 * description:
 */
public class GetUserSubscribeListApi extends FocusBaseApi {
    private PostUserId uidModel;

    public GetUserSubscribeListApi(String uid){
        uidModel = new PostUserId();
        uidModel.setUid(uid);
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUserSubscriptionList(uidModel);
    }
}
