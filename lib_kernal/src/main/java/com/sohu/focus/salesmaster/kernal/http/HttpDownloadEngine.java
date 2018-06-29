package com.sohu.focus.salesmaster.kernal.http;


import com.sohu.focus.salesmaster.kernal.http.convert.JacksonConverterFactory;
import com.sohu.focus.salesmaster.kernal.http.download.DownInfo;
import com.sohu.focus.salesmaster.kernal.http.download.DownState;
import com.sohu.focus.salesmaster.kernal.http.exception.HttpTimeException;
import com.sohu.focus.salesmaster.kernal.http.exception.RetryWhenNetworkException;
import com.sohu.focus.salesmaster.kernal.http.interceptor.FocusDownloadInterceptor;
import com.sohu.focus.salesmaster.kernal.http.subscribers.FocusDownSubscriber;
import com.sohu.focus.salesmaster.kernal.utils.EnvironmentManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sohu.focus.salesmaster.kernal.utils.NetUtil.getBaseUrl;
import static com.sohu.focus.salesmaster.kernal.utils.NetUtil.saveFile;
import static com.sohu.focus.salesmaster.kernal.utils.NetUtil.writeCache;

/**
 * Created by qiangzhao on 2016/11/15.
 */

public class HttpDownloadEngine {

    /*记录下载数据*/
    private Set<DownInfo> downInfos;
    /*回调sub队列*/
    private HashMap<String,FocusDownSubscriber> subMap;
    /*单利对象*/
    private volatile static HttpDownloadEngine INSTANCE;
    /*数据库类*/
    //TODO 实现数据库
//    private DbUtil db;

    private HttpDownloadEngine(){
        downInfos=new HashSet<>();
        subMap=new HashMap<>();
//        db=DbUtil.getInstance();
    }

    /**
     * 获取单例
     * @return
     */
    public static HttpDownloadEngine getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpDownloadEngine.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDownloadEngine();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 开始下载
     */
    public void startDown(final DownInfo info){
        /*正在下载不处理*/
        if(info==null||subMap.get(info.getUrl())!=null){
            subMap.get(info.getUrl()).setDownInfo(info);
            return;
        }
        /*添加回调处理类*/
        FocusDownSubscriber subscriber=new FocusDownSubscriber(info);
        /*记录回调sub*/
        subMap.put(info.getUrl(),subscriber);
        /*获取service，多次请求公用一个sercie*/
        BaseHttpService httpService;
        if(downInfos.contains(info)){
            httpService=info.getService();
        }else{
            FocusDownloadInterceptor interceptor = new FocusDownloadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OkHttpClient并设置超时时间
            builder.connectTimeout(info.getConnectonTime(), TimeUnit.SECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(EnvironmentManager.getSalesBaseUrl())
                    .build();
            httpService= retrofit.create(BaseHttpService.class);
            info.setService(httpService);
            downInfos.add(info);
        }
        /*得到rx对象-上一次下載的位置開始下載*/
        httpService.download("bytes=" + info.getReadLength() + "-",info.getUrl())
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, DownInfo>() {
                    @Override
                    public DownInfo call(ResponseBody responseBody) {
                        try {
                            if(info.isRange()) {
                                writeCache(responseBody,new File(info.getSavePath()),info);
                            } else {
                                saveFile(responseBody,new File(info.getSavePath()));
                            }
                        } catch (IOException e) {
                            /*失败抛出异常*/
                            throw new HttpTimeException(e.getMessage());
                        }
                        return info;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*数据回调*/
                .subscribe(subscriber);

    }


    /**
     * 停止下载
     */
    public void stopDown(DownInfo info){
        if(info==null)return;
        info.setState(DownState.STOP);
        info.getListener().onStop();
        if(subMap.containsKey(info.getUrl())) {
            FocusDownSubscriber subscriber=subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*保存数据库信息和本地文件*/
//        db.save(info);
    }


    /**
     * 暂停下载
     * @param info
     */
    public void pause(DownInfo info){
        if(info==null)return;
        info.setState(DownState.PAUSE);
        info.getListener().onPause();
        if(subMap.containsKey(info.getUrl())){
            FocusDownSubscriber subscriber=subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
//        db.update(info);
    }

    /**
     * 删除下载
     * @param info
     */
    public void delete(DownInfo info){
        if(subMap.containsKey(info.getUrl())){
            FocusDownSubscriber subscriber=subMap.get(info.getUrl());
            subscriber.unsubscribe();
            subMap.remove(info.getUrl());
        }
        /*这里需要讲info信息写入到数据中，可自由扩展，用自己项目的数据库*/
//        db.deleteDowninfo(info);
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown(){
        for (DownInfo downInfo : downInfos) {
            stopDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 暂停全部下载
     */
    public void pauseAll(){
        for (DownInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }


    /**
     * 返回全部正在下载的数据
     * @return
     */
    public Set<DownInfo> getDownInfos() {
        return downInfos;
    }

    /**
     * 移除下载数据
     * @param info
     */
    public void remove(DownInfo info){
        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }

}
