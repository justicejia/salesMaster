package com.sohu.focus.salesmaster.kernal.http;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sohu.focus.salesmaster.kernal.http.mapping.Mapper;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.io.Serializable;

/**
 * 实现Mapper{@link Mapper}的DTO基类
 * Created by zhaoqiang on 2017/4/24.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseMappingModel<VO> implements Serializable, Mapper<VO> {

    private int code = 0;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return CommonUtils.getDataNotNull(msg);
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    @Override
    public VO transform() {
        return null;
    }
}
