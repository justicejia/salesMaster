package com.sohu.focus.salesmaster.http;

/**
 * Created by luckyzhangx on 2017/11/1.
 */

public class HttpEngine {
    public static com.sohu.focus.salesmaster.kernal.http.HttpEngine getInstance() {
        return com.sohu.focus.salesmaster.kernal.http.HttpEngine.getInstance(HttpService.class);
    }
}
