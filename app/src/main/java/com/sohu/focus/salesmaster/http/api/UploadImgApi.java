package com.sohu.focus.salesmaster.http.api;

import com.sohu.focus.salesmaster.http.FocusBaseApi;
import com.sohu.focus.salesmaster.http.HttpService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 上传图片api
 * Created by yuanminjia on 2017/11/3.
 */

public class UploadImgApi extends FocusBaseApi {
    private File mFile;

    public UploadImgApi(File file) {
        mFile = file;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), mFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", mFile.getName(), body);
        return methods.uploadImg(part);
    }
}
