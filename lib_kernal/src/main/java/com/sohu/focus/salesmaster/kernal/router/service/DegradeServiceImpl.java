package com.sohu.focus.salesmaster.kernal.router.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

/**
 * 降级策略
 * Created by zhaoqiang on 2017/8/24.
 */
@Route(path = "/kernal/degrade")
public class DegradeServiceImpl implements DegradeService{
    @Override
    public void onLost(Context context, Postcard postcard) {

    }

    @Override
    public void init(Context context) {

    }
}
