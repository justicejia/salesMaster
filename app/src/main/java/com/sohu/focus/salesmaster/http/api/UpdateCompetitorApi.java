package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.UpdateCompetitorModel;

import rx.Observable;

/**
 * Created by yuanminjia on 2018/1/12.
 */

public class UpdateCompetitorApi extends FocusBaseApi {
    private UpdateCompetitorModel model;

    public UpdateCompetitorApi(UpdateCompetitorModel model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.updateCompetitor(model);
    }
}
