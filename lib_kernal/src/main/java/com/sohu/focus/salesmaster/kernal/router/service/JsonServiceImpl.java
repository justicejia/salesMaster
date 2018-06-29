package com.sohu.focus.salesmaster.kernal.router.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

/**
 * 解析json
 * Created by zhaoqiang on 2017/8/24.
 */
@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {
    @Override
    public void init(Context context) {

    }

    @Override
    public <T> T json2Object(String text, Class<T> clazz) {
        return CommonUtils.jsonStringToObject(text, clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return CommonUtils.objectToJsonString(instance);
    }
}
