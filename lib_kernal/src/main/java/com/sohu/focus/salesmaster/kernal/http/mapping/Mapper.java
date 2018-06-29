package com.sohu.focus.salesmaster.kernal.http.mapping;


/**
 * DTO或者DAO实现该接口，用于转换为VO
 * @param <VO>  VIEW中实际使用的对象
 *
 * Created by zhaoqiang on 2017/4/24.
 */
public interface Mapper<VO> {
    VO transform();
}
