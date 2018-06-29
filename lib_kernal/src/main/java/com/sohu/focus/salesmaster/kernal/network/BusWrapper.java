package com.sohu.focus.salesmaster.kernal.network;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public interface BusWrapper {
    void register();

    void unregister();

    void post(Object event);
}