package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.dynamics.model.PublishDynamicsModel;
import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;

import rx.Observable;

/**
 * 发布动态
 * Created by yuanminjia on 2017/11/3.
 */

public class PublishDynamicsApi extends FocusBaseApi {
    PublishDynamicsModel mModel;

    public PublishDynamicsApi(PublishDynamicsModel model) {
        mModel = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.publishDynamic(mModel);
    }
}
