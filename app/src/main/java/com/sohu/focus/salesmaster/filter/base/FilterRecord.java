package com.sohu.focus.salesmaster.filter.base;

import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

/**
 * Created by luckyzhangx on 05/01/2018.
 */

public class FilterRecord {
    String parentFieldName;
    String fieldName;
    String fieldValue;

    FilterVO filter;

    public FilterRecord(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public FilterRecord(String fieldName, String fieldValue, String parentFieldName) {
        this(fieldName, fieldValue);
        this.parentFieldName = parentFieldName;
    }

    public FilterVO getFilter() {
        return filter;
    }

    public void setFilter(FilterVO filter) {
        this.filter = filter;
    }

    public FilterRecord subFilterRecord;

    public FilterRecord getSubFilterRecord() {
        return subFilterRecord;
    }

    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
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

    public void setSubFilterRecord(FilterRecord subFilterRecord) {
        this.subFilterRecord = subFilterRecord;
    }
}
