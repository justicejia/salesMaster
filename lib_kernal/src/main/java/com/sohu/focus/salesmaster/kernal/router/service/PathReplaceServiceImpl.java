package com.sohu.focus.salesmaster.kernal.router.service;

import android.content.Context;
import android.net.Uri;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;

/**
 * 重写跳转url
 * Created by zhaoqiang on 2017/8/24.
 */
@Route(path = "/kernal/replace")
public class PathReplaceServiceImpl implements PathReplaceService {

    @Override
    public String forString(String path) {
        return path;// 按照一定的规则处理之后返回处理后的结果
    }

    @Override
    public Uri forUri(Uri uri) {
        return uri;
    }


    @Override
    public void init(Context context) {

    }
}
