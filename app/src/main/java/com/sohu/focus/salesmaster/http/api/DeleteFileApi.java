package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.DeleteFile;

import rx.Observable;

/**
 * Created by yuanminjia on 2018/2/6.
 */

public class DeleteFileApi extends FocusBaseApi {
    DeleteFile model;

    public DeleteFileApi(String id) {
        model = new DeleteFile();
        model.setId(id);
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.deleteFile(model);
    }
}
