package com.sohu.focus.salesmaster.filter;

import com.sohu.focus.salesmaster.base.SalesConstants;

/**
 * Created by luckyzhangx on 17/01/2018.
 */

public class SortFilerHelper {
    //    排序
    private static final String SCORE = "score";
    private static final String AD = "ad_money";
    private static final String CALL = "is_called_distinct";

    /**
     * @param fieldValue 排序筛选项的参数值
     * @return {@link SalesConstants#SORT_SCORE},
     * {@link SalesConstants#SORT_AD} or
     * {@link SalesConstants#SORT_CALL}
     */

    public static int getType(String fieldValue) {
        switch (fieldValue) {
            case SCORE:
                return SalesConstants.SORT_SCORE;
            case AD:
                return SalesConstants.SORT_AD;
            case CALL:
                return SalesConstants.SORT_CALL;
        }
        return -1;
    }
}
