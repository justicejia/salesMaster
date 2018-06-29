package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;

import rx.Observable;

/**
 * Created by yuanminjia on 2018/1/10.
 */

public class GetProjectDataApi extends FocusBaseApi {
    private String mEstateId;

    public GetProjectDataApi(String estateId) {
        mEstateId = estateId;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        GetProjectModel model = new GetProjectModel();
        model.setType("data");
        model.setEstateId(mEstateId);
        return methods.getProjectData(model);
    }
}
