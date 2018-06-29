package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.AddClient;

import rx.Observable;

/**
 * Created by luckyzhangx on 10/01/2018.
 */

public class AddClientApi extends FocusBaseApi {
    private AddClient addClient;

    public void setClientModel(AddClient addClient) {
        this.addClient = addClient;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.addClient(addClient);
    }
}
