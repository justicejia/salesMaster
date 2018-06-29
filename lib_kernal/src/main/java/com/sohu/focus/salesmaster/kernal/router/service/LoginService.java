package com.sohu.focus.salesmaster.kernal.router.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by zhaoqiang on 2017/8/24.
 */

public interface LoginService extends IProvider {

    boolean isLogin();

    void startLogin(Context context);
}
