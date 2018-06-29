package com.sohu.focus.salesmaster.kernal.network.online;

/**
 * Created by zhaoqiang on 2017/9/26.
 */

public interface OnlineChecker {
    void check();

    void setPingParameters(String host, int port, int timeoutInMs);
}
