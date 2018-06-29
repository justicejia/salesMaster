package com.sohu.focus.salesmaster.kernal.http.subscribers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sohu.focus.salesmaster.kernal.bus.RxBus;
import com.sohu.focus.salesmaster.kernal.bus.RxEvent;
import com.sohu.focus.salesmaster.kernal.http.BaseApi;
import com.sohu.focus.salesmaster.kernal.http.BaseMappingModel;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestMappingListener;
import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.bus.EventValues;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;
import com.sohu.focus.salesmaster.kernal.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

import static com.sohu.focus.salesmaster.kernal.KernalConstants.BUILD_STATUS_CODE_SUCCESS;
import static com.sohu.focus.salesmaster.kernal.KernalConstants.BUSINESS_STATUS_CODE_SUCCESS;
import static com.sohu.focus.salesmaster.kernal.KernalConstants.BUSINESS_STATUS_USER_NOT_LOGIN;
import static com.sohu.focus.salesmaster.kernal.KernalConstants.BUSINESS_STATUS_USER_TOKEN_EXPIRED;
import static com.sohu.focus.salesmaster.kernal.KernalConstants.ERROR_MSG_NETWORK_ERROR;
import static com.sohu.focus.salesmaster.kernal.KernalConstants.ERROR_MSG_NET_DISCONNECTED;


/**
 * Created by qiangzhao on 2016/11/14.
 */

public class FocusRequestMappingSubscriber<DTO extends BaseMappingModel<VO>, VO> extends Subscriber<DTO> {

    //    回调接口
    private HttpRequestMappingListener<DTO, VO> mSubscriberOnNextListener;

    private BaseApi api;

    /**
     * 构造Api
     *
     * @param api
     */
    public FocusRequestMappingSubscriber(BaseApi api, HttpRequestMappingListener<DTO, VO> listenerSoftReference) {
        this.api = api;
        this.mSubscriberOnNextListener = listenerSoftReference;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        errorDo(e);
    }

    /*错误统一处理*/
    private void errorDo(Throwable e) {
        if (!api.isSkipErrToast()) {
            if (e instanceof SocketTimeoutException) {
                ToastUtil.toast(ERROR_MSG_NET_DISCONNECTED);
            } else if (e instanceof ConnectException) {
                ToastUtil.toast(ERROR_MSG_NET_DISCONNECTED);
            } else if (e instanceof HttpException) {
                ToastUtil.toast(ERROR_MSG_NET_DISCONNECTED);
            } else if (e instanceof UnknownHostException) {
                ToastUtil.toast(ERROR_MSG_NET_DISCONNECTED);
            } else if (e instanceof JsonParseException) {
                ToastUtil.toast(ERROR_MSG_NETWORK_ERROR);
            } else if (e instanceof JsonMappingException) {
                Logger.ZQ().e("Json Error : " + e.getMessage());
            } else {
                if (FocusLog.isDebugging)
                    ToastUtil.toast("发生错误" + e.getMessage());
                Logger.ZQ().e("Http Request Error : " + e.getMessage());
            }
        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(DTO t) {
        //楼盘域名下的成功code是1，和其他的code处理分开
        if (CommonUtils.notEmpty(api.getCustomBaseUrl()) && api.getCustomBaseUrl().equals(EnvironmentManager.getBuildBaseUrl())) {
            if (t.getCode() == BUILD_STATUS_CODE_SUCCESS) {
                mSubscriberOnNextListener.onSuccess(t.transform());
            } else {
                mSubscriberOnNextListener.onFailed(t, api == null ? "" : api.getApiUrl());
            }
        } else {
            if (t.getCode() == BUSINESS_STATUS_CODE_SUCCESS) {
                mSubscriberOnNextListener.onSuccess(t.transform());
            } else if (t.getCode() == BUSINESS_STATUS_USER_NOT_LOGIN
                    || t.getCode() == BUSINESS_STATUS_USER_TOKEN_EXPIRED) {
                RxEvent rxEvent = new RxEvent();
                rxEvent.setTag(EventValues.TAG_LOGOUT);
                RxBus.get().post(rxEvent);
            } else {
                mSubscriberOnNextListener.onFailed(t, api == null ? "" : api.getApiUrl());
            }
        }
    }
}
