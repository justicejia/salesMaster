package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;
import com.sohu.focus.salesmaster.http.model.GetFiles;

import rx.Observable;

/**
 * 获取文件列表
 * Created by yuanminjia on 2018/2/6.
 */

public class GetFileListApi extends FocusBaseApi {
    private GetFiles mFileModel;

    public GetFileListApi(String estateId, int pageSize, int pageNum) {
        mFileModel = new GetFiles();
        mFileModel.setEstateId(estateId);
        mFileModel.setPage(pageNum);
        mFileModel.setPageSize(pageSize);
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getProjectFileList(mFileModel);
    }
}
