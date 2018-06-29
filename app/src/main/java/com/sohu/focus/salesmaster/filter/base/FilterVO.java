package com.sohu.focus.salesmaster.filter.base;


import android.os.UserManager;

import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;
import com.sohu.focus.salesmaster.login.AccountManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyzhangx on 05/01/2018.
 */

public class FilterVO {

    public FilterVO(String desc, String fieldName, String fieldValue) {
        this.desc = desc;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    //    展示名称
    public String desc;
    //    父级筛选项的字段名称(可选)
    public String parentFieldName;
    //    服务器对应字段名称，针对以 key-value 形式提供筛选参数的接口
    public String fieldName;
    //    服务器对应的字段值
    public String fieldValue;

    public List<FilterVO> subFilterModels;

    public String getDesc() {
        return CommonUtils.getDataNotNull(desc);
    }

    public String getParentFieldName() {
        return CommonUtils.getDataNotNull(parentFieldName);
    }

    public String getFieldName() {
        return CommonUtils.getDataNotNull(fieldName);
    }

    public String getFieldValue() {
        return CommonUtils.getDataNotNull(fieldValue);
    }

    public List<FilterVO> getSubFilterModels() {
        if (subFilterModels == null) {
            return new ArrayList<>();
        }
        return subFilterModels;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof FilterVO)) return false;
        FilterVO vo = (FilterVO) obj;
        if (fieldName.equals(vo.fieldName) && fieldValue.equals(vo.fieldValue)) return true;
        return false;
    }
}
