package com.sohu.focus.salesmaster.newFilter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jiayuanmin on 2018/5/18
 * description:
 */
@IntDef({FilterType.CITY, FilterType.ROLE, FilterType.ORDER, FilterType.PROGRESS})
@Retention(RetentionPolicy.SOURCE)
public @interface FilterType {
    int CITY = 0;
    int ROLE = 1;
    int ORDER = 2;
    int PROGRESS = 3;
}
