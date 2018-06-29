package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetProjectModel;

import rx.Observable;

/**
 * Created by luckyzhangx on 11/01/2018.
 */

public class GetClientsApi extends FocusBaseApi {
    private GetProjectModel model;

    public void setModel(GetProjectModel model) {
        this.model = model;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getClients(model);
    }
}
