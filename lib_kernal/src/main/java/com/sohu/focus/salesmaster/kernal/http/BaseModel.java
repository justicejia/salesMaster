package com.sohu.focus.salesmaster.kernal.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * 所有的服务器返回接口Model继承自本类
 * Created by qiangzhao on 2016/12/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseModel implements Serializable{

    private int code = 0;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return CommonUtils.getDataNotNull(msg);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String message) {
        this.msg = message;
    }
}
