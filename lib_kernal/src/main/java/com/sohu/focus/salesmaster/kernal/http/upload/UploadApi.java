package com.sohu.focus.salesmaster.kernal.http.upload;


import com.sohu.focus.salesmaster.kernal.http.BaseApi;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by qiangzhao on 2016/12/13.
 */

public class UploadApi extends BaseApi {

    /*需要上传的文件*/
    private MultipartBody.Part part;

    private Map<String, String> params;


    public UploadApi() {
        setApiUrl("v6/note/uploadImg");
        setCache(false);
    }

    public void upload(String filePath) {
        /*使用默认的requestBody没有多次writeTo的问题*/
        setShowLog(true);
        File file = new File(filePath);
        RequestBody requestBody= RequestBody.create(MediaType.parse("image/jpeg"),file);
        part= MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }


    public void upload(String filePath, UploadProgressListener progressListener) {
        /*上传的时候，如果使用logging拦截器，会导致requestBody的writeTo调用多次，所以必须设置为false*/
        setShowLog(false);
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"),file);
        part= MultipartBody.Part.createFormData("file", "image.jpg", new ProgressRequestBody(requestBody, progressListener));
    }

    @Override
    public Observable getObservable(Object methods) {
//        RequestBody noteId = RequestBody.create(MediaType.parse("text/plain"), "110");
//        return methods.uploadImage(noteId, part, params);
        return null;
    }
}
