package com.sohu.focus.salesmaster.kernal.http;

import android.os.Parcel;
import android.os.Parcelable;

import rx.functions.Func1;

/**
 * Created by qiangzhao on 2016/11/14.
 */

public class HttpResult<T> extends BaseModel implements Func1<HttpResult<T>, T>, Parcelable{

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpResult(){}

    protected HttpResult(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HttpResult> CREATOR = new Creator<HttpResult>() {
        @Override
        public HttpResult createFromParcel(Parcel in) {
            return new HttpResult(in);
        }

        @Override
        public HttpResult[] newArray(int size) {
            return new HttpResult[size];
        }
    };

    @Override
    public T call(HttpResult<T> httpResult) {
//        if(httpResult.getCode() != LiveConstants.BUSINESS_STATUS_CODE_SUCCESS) {
//            throw new HttpTimeException(httpResult.getMsg());
//        }
        return httpResult.getData();
    }
}
