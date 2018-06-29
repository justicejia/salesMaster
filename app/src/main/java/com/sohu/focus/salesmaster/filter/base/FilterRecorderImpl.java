package com.sohu.focus.salesmaster.filter.base;

import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by luckyzhangx on 05/01/2018.
 */

public class FilterRecorderImpl implements FilterRecorder {

    protected List<FilterRecord> records = new ArrayList<>();

    //    直接插入到第一级
    @Override
    public void insert(FilterRecord record) {
        //        是否存在父节点
        if (CommonUtils.notEmpty(record.parentFieldName)) {
//            若指明了父节点字段名，父节点不存在时，将不会插入
            FilterRecord parentRecord = getRecord(record.parentFieldName);

            if (parentRecord != null) {
                parentRecord.setSubFilterRecord(record);
                return;
            }
        } else {
//            是否存在兄弟节点
            FilterRecord record1 = getRecord(record.fieldName);
            if (record1 == null) {
//                没有父节点，也没有兄弟节点，直接添加到第一级
                records.add(record);
                return;
            } else {
//             有兄弟节点
//             再次检查兄弟节点是否有父节点
                FilterRecord parentRecord2 = getRecord(record1.parentFieldName);
                if (parentRecord2 != null) {
//                    有父节点，替换
                    parentRecord2.setSubFilterRecord(record);
                    return;
                } else {
//                    兄弟节点没有父节点，说明是第一级节点，直接替换
                    records.remove(record1);
                    records.add(record);
                    return;
                }
            }

        }
    }

    @Override
    public void remove(String fieldName) {
        FilterRecord record = getRecord(fieldName);
        if (record == null) return;
        if (CommonUtils.isEmpty(record.parentFieldName))
            records.remove(record);
        else
            getRecord(record.parentFieldName).setSubFilterRecord(null);
    }

    @Override
    public void clearAll() {
        records.clear();
    }

    @Override
    public FilterRecord getRecord(String fieldName) {
        FilterRecord record = null;
        for (FilterRecord recorditem : records) {
            record = getRecord(fieldName, recorditem);
            if (record != null) {
                break;
            }
        }

        return record;
    }

    @Override
    public Collection<FilterRecord> getRecords(boolean flat) {
        if (flat) {
            List<FilterRecord> recordList = new ArrayList<>();
            FilterRecord tempRecord;
            for (FilterRecord record : records) {
                tempRecord = record;
                while (tempRecord != null) {
                    recordList.add(tempRecord);
                    tempRecord = tempRecord.getSubFilterRecord();
                }
            }
            return recordList;
        } else
            return records;
    }

    //    一些辅助方法

    //    查找筛选链上有没有相关 fieldName 的 FilterRecord
    private FilterRecord getRecord(String fieldName, FilterRecord record) {
        FilterRecord queryRecord = record;
        while (queryRecord != null) {
            if (queryRecord.fieldName.equals(fieldName)) return queryRecord;
            queryRecord = queryRecord.getSubFilterRecord();
        }
        return null;
    }
}
