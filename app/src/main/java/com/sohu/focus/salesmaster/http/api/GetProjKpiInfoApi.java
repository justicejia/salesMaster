package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;

import rx.Observable;

/**
 * Created by luckyzhangx on 02/02/2018.
 */

public class GetProjKpiInfoApi extends FocusBaseApi {
    private String mEstateId;

    public GetProjKpiInfoApi(String estateId) {
        mEstateId = estateId;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        GetProjectModel model = new GetProjectModel();
        model.setType("kpi");
        model.setEstateId(mEstateId);
        return methods.getProjectKpiInfo(model);
    }
}
