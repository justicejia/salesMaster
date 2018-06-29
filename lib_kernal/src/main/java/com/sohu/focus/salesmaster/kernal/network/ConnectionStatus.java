package com.sohu.focus.salesmaster.kernal.network;

/**
 * 网络状态枚举类
 * Created by zhaoqiang on 2017/9/26.
 */

public enum ConnectionStatus {
    UNKNOWN("unknown"),
    WIFI_CONNECTED("connected to WiFi"),
    WIFI_CONNECTED_HAS_INTERNET("connected to WiFi (Internet available)"),
    WIFI_CONNECTED_HAS_NO_INTERNET("connected to WiFi (Internet not available)"),
    MOBILE_CONNECTED("connected to mobile network"),
    OFFLINE("offline");

    private final String status;

    ConnectionStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}