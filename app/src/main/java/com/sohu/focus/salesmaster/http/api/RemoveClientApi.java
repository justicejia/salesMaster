package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.RemoveClient;

import rx.Observable;

/**
 * Created by luckyzhangx on 12/01/2018.
 */

public class RemoveClientApi extends FocusBaseApi {
    RemoveClient removeClient;

    public void setRemoveClient(RemoveClient removeClient) {
        this.removeClient = removeClient;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        if (removeClient == null) return null;
        return methods.removeClient(removeClient);
    }
}
