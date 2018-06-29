package com.sohu.focus.salesmaster.filter.base;

/**
 * Created by luckyzhangx on 09/01/2018.
 */

public class FilterRecorderHelper {
    public static void Record(FilterRecorder recorder, FilterVO filter) {
        FilterRecord record = new FilterRecord(filter.fieldName, filter.fieldValue, filter.parentFieldName);
        record.setFilter(filter);
        recorder.insert(record);
    }
}
