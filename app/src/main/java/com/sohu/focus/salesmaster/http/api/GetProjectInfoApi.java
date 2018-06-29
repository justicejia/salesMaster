package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;

import rx.Observable;

/**
 * Created by yuanminjia on 2017/12/7.
 */

public class GetProjectInfoApi extends FocusBaseApi {
    private String mEstateId;

    public GetProjectInfoApi(String estateId) {
        mEstateId = estateId;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        GetProjectModel model = new GetProjectModel();
        model.setType("info");
        model.setEstateId(mEstateId);
        return methods.getProjectInfo(model);
    }
}
