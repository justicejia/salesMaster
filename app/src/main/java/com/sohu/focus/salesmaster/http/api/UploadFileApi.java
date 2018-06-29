package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 上传文件api
 * Created by yuanminjia on 2018/2/6.
 */

public class UploadFileApi extends FocusBaseApi {
    private File mFile;
    private String mEstateId;

    public UploadFileApi(File file, String estateId) {
        mFile = file;
        mEstateId = estateId;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        RequestBody body = RequestBody.create(MediaType.parse("*/*"), mFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData(mEstateId, mFile.getName(), body);
        return methods.uploadFile(part);
    }
}
