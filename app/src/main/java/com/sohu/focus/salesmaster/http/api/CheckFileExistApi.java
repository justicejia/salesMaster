package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.PostCheckFileModel;

import rx.Observable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class CheckFileExistApi extends FocusBaseApi {
    PostCheckFileModel model;

    public CheckFileExistApi(String estateId, String fileName) {
        model = new PostCheckFileModel();
        model.setEstateId(estateId);
        model.setFileName(fileName);
    }


    @Override
    public Observable getObservable(HttpService methods) {
        return methods.checkFile(model);
    }
}
