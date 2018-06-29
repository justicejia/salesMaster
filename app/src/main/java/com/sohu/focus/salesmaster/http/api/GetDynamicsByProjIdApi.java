package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetDynamicsByProjId;

import rx.Observable;

/**
 * Created by luckyzhangx on 11/6/2017.
 */

public class GetDynamicsByProjIdApi extends FocusBaseApi {

    GetDynamicsByProjId projectId;

    public void setProjectId(GetDynamicsByProjId projectId) {
        this.projectId = projectId;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getDynamicsByProjId(projectId);
    }
}
