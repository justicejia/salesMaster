package com.sohu.focus.salesmaster.filter;

import com.sohu.focus.salesmaster.filter.base.FilterRecord;
import com.sohu.focus.salesmaster.filter.base.FilterRecorderImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyzhangx on 15/01/2018.
 */

public class SalesFilterRecorder extends FilterRecorderImpl {

    public List<String> getStringList(String fieldName) {
        FilterRecord record = getRecord(fieldName);
        if (record == null) return null;
        List<String> list = new ArrayList<>();
//        后台需要 int 型，临时处理方法
        if (record.getFieldValue().equals("-1")) {
            return null;
        } else
            list.add(record.getFieldValue());

        return list;
    }
}
