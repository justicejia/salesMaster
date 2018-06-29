package com.sohu.focus.salesmaster.filter.base;

import java.util.Collection;

/**
 * Created by luckyzhangx on 05/01/2018.
 */

public interface FilterRecorder {

    void insert(FilterRecord record);

    void remove(String fieldName);

    void clearAll();

    FilterRecord getRecord(String fi2eldName);

    Collection<FilterRecord> getRecords(boolean flatRecords);

}
