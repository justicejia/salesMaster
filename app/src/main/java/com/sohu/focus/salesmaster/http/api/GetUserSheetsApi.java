package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostUserSheetsModel;

import rx.Observable;

/**
 * Created by jiayuanmin on 2018/5/29
 * description:
 */
public class GetUserSheetsApi extends FocusBaseApi {

    private PostUserSheetsModel model;

    public GetUserSheetsApi(String uid, int areaCode, int salesRole) {
        model = new PostUserSheetsModel();
        model.areaCode = areaCode;
        model.personId = uid;
        model.salesRole = salesRole;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getUserSubscribedSheets(model);
    }
}
