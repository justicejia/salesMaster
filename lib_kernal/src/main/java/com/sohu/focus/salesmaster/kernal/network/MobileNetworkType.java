package com.sohu.focus.salesmaster.kernal.network;

/**
 * 手机网络枚举类
 * <p>
 * Created by zhaoqiang on 2017/9/26.
 */

public enum MobileNetworkType {
    UNKNOWN("unknown"),
    LTE("LTE"),
    HSPAP("HSPAP"),
    EDGE("EDGE"),
    GPRS("GPRS");

    private final String type;

    MobileNetworkType(String status) {
        this.type = status;
    }

    @Override
    public String toString() {
        return type;
    }
}
