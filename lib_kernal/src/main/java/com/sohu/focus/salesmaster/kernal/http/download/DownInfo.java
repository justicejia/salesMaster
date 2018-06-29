package com.sohu.focus.salesmaster.kernal.http.download;


import com.sohu.focus.salesmaster.kernal.http.BaseHttpService;
import com.sohu.focus.salesmaster.kernal.http.listener.HttpDownListener;

/**
 * apk下载请求数据基础类
 * Created by WZG on 2016/10/20.
 */

//@Entity
public class DownInfo {
//    @Id
    private long id;
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载唯一的HttpService*/
//    @Transient
    private BaseHttpService service;
    /*回调监听*/
//    @Transient
    private HttpDownListener listener;

    /*超时设置*/
    private  int connectonTime=6;
    /*state状态数据库保存*/
    private int stateInte;
    /*url*/
    private String url;
    /**是否使用断点续传*/
    private boolean isRange = false;

    public DownInfo(String url, HttpDownListener listener) {
        setUrl(url);
        setListener(listener);
    }

    public DownInfo(String url) {
        setUrl(url);
    }

    public boolean isRange() {
        return isRange;
    }

    public void setRange(boolean range) {
        isRange = range;
    }

    public DownState getState() {
        switch (getStateInte()){
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }


    public int getStateInte() {
        return stateInte;
    }

    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }

    public HttpDownListener getListener() {
        return listener;
    }

    public void setListener(HttpDownListener listener) {
        this.listener = listener;
    }

    public BaseHttpService getService() {
        return service;
    }

    public void setService(BaseHttpService service) {
        this.service = service;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }


    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }


    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getConnectonTime() {
        return this.connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }
}
