package com.sohu.focus.salesmaster.kernal.http;

import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestListener;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpRequestMappingListener;
import com.sohu.focus.salesmaster.kernal.http.subscribers.FocusRequestMappingSubscriber;
import com.sohu.focus.salesmaster.kernal.http.subscribers.FocusRequestSubscriber;
import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.Logger;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by qiangzhao on 2016/11/9.
 */

public class HttpEngine {

    private Map<String, List<WeakReference<Subscription>>> requestMap = new HashMap();

    private Class<?> moduleHttpServiceClazz;

    private HashSet<Integer> hitCaches = new HashSet<>();

    private HttpEngine(Class<?> service) {
        moduleHttpServiceClazz = service;
    }

    /*
        must call this in application first
     */
    public static HttpEngine addModuleHttpService(Class<?> moduleClazz) {
        HttpEngine moduleHttpEngine = new HttpEngine(moduleClazz);
        HttpEngineHolder.instance.put(moduleClazz, moduleHttpEngine);
        return moduleHttpEngine;
    }

    public static HttpEngine getInstance(Class<?> moduleClazz) {
        HttpEngine engine = HttpEngineHolder.instance.get(moduleClazz);
        if (engine == null) {
            return addModuleHttpService(moduleClazz);
        }
        return engine;
    }

    public static void cancelAll() {
        for (Map.Entry<Class<?>, HttpEngine> entry : HttpEngineHolder.instance.entrySet()) {
            entry.getValue().cancelAllInternal();
        }

    }

    public <T extends BaseModel> void doHttpRequest(BaseApi api, HttpRequestListener<T> listener) {
        if (api == null)
            return;
        //如果非debug，强制log关闭
        if (!FocusLog.isDebugging) {
            api.setShowLog(false);
        }
        Retrofit retrofit = RetrofitFactory.createRetrofit(api);
        FocusRequestSubscriber<T> focusRequestSubscriber = new FocusRequestSubscriber<>(api, listener);
        Observable observable = api.getObservable(retrofit.create(moduleHttpServiceClazz))
                /*失败后的retry配置*/
//                .retryWhen(new RetryWhenNetworkException())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());

        /*数据回调*/
        Subscription subscription = observable.subscribe(focusRequestSubscriber);
        addRequestCache(api.getTag(), subscription);
    }

    public <DTO extends BaseMappingModel<VO>, VO> void doHttpRequest(BaseApi api, HttpRequestMappingListener<DTO, VO> listener) {
        if (api == null)
            return;
        //如果非debug，强制log关闭
        if (!FocusLog.isDebugging) {
            api.setShowLog(false);
        }
        Retrofit retrofit = RetrofitFactory.createRetrofit(api);
        FocusRequestMappingSubscriber<DTO, VO> focusRequestSubscriber = new FocusRequestMappingSubscriber<>(api, listener);
        Observable observable = api.getObservable(retrofit.create(moduleHttpServiceClazz))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());

        /*数据回调*/
        Subscription subscription = observable.subscribe(focusRequestSubscriber);
        addRequestCache(api.getTag(), subscription);
    }

    public <T extends BaseModel> void doCacheRequest(final BaseApi api, final HttpRequestListener<T> listener) {
        if (api == null)
            return;
        //如果非debug，强制log关闭
        if (!FocusLog.isDebugging) {
            api.setShowLog(false);
        }
        api.generateCacheKey();
        Retrofit retrofit = RetrofitFactory.createRetrofit(api);
        Retrofit cacheRetrofit = RetrofitFactory.createCacheRetrofit(api);

        FocusRequestSubscriber<T> focusRequestSubscriber = new FocusRequestSubscriber<>(api, listener);

        Observable fromCache = api.getObservable(cacheRetrofit.create(moduleHttpServiceClazz)).subscribeOn(Schedulers.io());
        if (api.isForceHttp()) {
            doHttpRequest(api, listener);
            return;
        } else if (!hitCaches.contains(RetrofitFactory.generateCacheKey(api))) {
            fromCache.unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.ZQ().d("cache not hit by [" + api.getCompleteUrl() + "]");
                            doHttpRequest(api, listener);
                            RetrofitFactory.removeNoCacheRetrofit(api);
                        }

                        @Override
                        public void onNext(Object o) {
                            Logger.ZQ().d("cache hit by [" + api.getCompleteUrl() + "], add to hitCaches");
                            hitCaches.add(RetrofitFactory.generateCacheKey(api));
                            doCacheRequest(api, listener);
                        }
                    });
            return;
        }
        Logger.ZQ().d("cache hit by [" + api.getCompleteUrl() + "]");
        Observable fromNetwork = api.getObservable(retrofit.create(moduleHttpServiceClazz)).subscribeOn(Schedulers.io());

        Observable concat = Observable.concat(fromCache, fromNetwork)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        /*数据回调*/
        Subscription subscription = concat.subscribe(focusRequestSubscriber);
        addRequestCache(api.getTag(), subscription);
    }

    public <DTO extends BaseMappingModel<VO>, VO> void doCacheRequest(final BaseApi api, final HttpRequestMappingListener<DTO, VO> listener) {
        if (api == null)
            return;
        //如果非debug，强制log关闭
        if (!FocusLog.isDebugging) {
            api.setShowLog(false);
        }
        api.generateCacheKey();
        Retrofit retrofit = RetrofitFactory.createRetrofit(api);
        Retrofit cacheRetrofit = RetrofitFactory.createCacheRetrofit(api);
        FocusRequestMappingSubscriber<DTO, VO> focusRequestSubscriber = new FocusRequestMappingSubscriber<>(api, listener);
        Observable fromCache = api.getObservable(cacheRetrofit.create(moduleHttpServiceClazz)).subscribeOn(Schedulers.io());
        if (api.isForceHttp()) {
            doHttpRequest(api, listener);
            return;
        } else if (!hitCaches.contains(RetrofitFactory.generateCacheKey(api))) {
            fromCache.unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.ZQ().d("cache not hit by [" + api.getCompleteUrl() + "]");
                            doHttpRequest(api, listener);
                            RetrofitFactory.removeNoCacheRetrofit(api);
                        }

                        @Override
                        public void onNext(Object o) {
                            Logger.ZQ().d("cache hit by [" + api.getCompleteUrl() + "], add to hitCaches");
                            hitCaches.add(RetrofitFactory.generateCacheKey(api));
                            doCacheRequest(api, listener);
                        }
                    });
            return;
        }
        Logger.ZQ().d("cache hit by [" + api.getCompleteUrl() + "]");
        Observable fromNetwork = api.getObservable(retrofit.create(moduleHttpServiceClazz)).subscribeOn(Schedulers.io());

        Observable concat = Observable.concat(fromCache, fromNetwork)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /*数据回调*/
        Subscription subscription = concat.subscribe(focusRequestSubscriber);
        addRequestCache(api.getTag(), subscription);
    }

    private void addRequestCache(String tag, Subscription subscription) {
        if (requestMap.containsKey(tag)) {
            List<WeakReference<Subscription>> requests = requestMap.get(tag);
            requests.add(new WeakReference<>(subscription));
        } else {
            List<WeakReference<Subscription>> requests = new ArrayList<>();
            requests.add(new WeakReference<>(subscription));
            requestMap.put(tag, requests);
        }
    }

    public void cancel(String tag) {
        if (requestMap.get(tag) != null) {
            List<WeakReference<Subscription>> requests = requestMap.get(tag);
            for (WeakReference<Subscription> request : requests) {
                if (request.get() != null) {
                    request.get().unsubscribe();
                }
            }
            requestMap.remove(tag);
        }
        Map<Integer, OkHttpClient> clients = RetrofitFactory.getOkHttpClientCache();
        if (CommonUtils.notEmpty(clients)) {
            for (Map.Entry<Integer, OkHttpClient> client : clients.entrySet()) {
                cancelRequest(client.getValue(), tag);
            }

        }
    }

    private void cancelAllInternal() {
        for (Map.Entry<String, List<WeakReference<Subscription>>> entry : requestMap.entrySet()) {
            List<WeakReference<Subscription>> requests = entry.getValue();
            if (CommonUtils.notEmpty(requests)) {
                for (WeakReference<Subscription> request : requests) {
                    if (request.get() != null) {
                        request.get().unsubscribe();
                    }
                }
            }
        }
        Map<Integer, OkHttpClient> clients = RetrofitFactory.getOkHttpClientCache();
        if (CommonUtils.notEmpty(clients)) {
            for (Map.Entry<Integer, OkHttpClient> client : clients.entrySet()) {
                cancelAllRequest(client.getValue());
            }
        }
        requestMap.clear();
    }

    /**
     * 根据Tag取消请求
     */
    private void cancelRequest(OkHttpClient client, Object tag) {
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    private void cancelAllRequest(OkHttpClient client) {
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    public void clearCache() {
        List<OkHttpClient> clients = RetrofitFactory.getOkHttpClientCache2();
        if(CommonUtils.notEmpty(clients)) {
            for(OkHttpClient client : clients) {
                try {
                    if (client.cache() != null)
                        client.cache().evictAll();
                    hitCaches.clear();
                } catch (IOException e) {
                    Logger.ZQ().e("clear okhttp cache err : " + new Throwable(e));
                }
            }
        }
    }

    private static class HttpEngineHolder {
        private static Map<Class<?>, HttpEngine> instance = new ConcurrentHashMap<>();
    }
}
