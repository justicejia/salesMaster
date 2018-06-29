package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostProjectIdModel;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/4/23
 * description:
 */
public class GetProjectInvestApi extends FocusBaseApi {
    private String pid;

    public GetProjectInvestApi(String pid) {
        this.pid = pid;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        PostProjectIdModel model = new PostProjectIdModel();
        model.setEstateId(pid);
        return methods.getProjectInvest(model);
    }
}
