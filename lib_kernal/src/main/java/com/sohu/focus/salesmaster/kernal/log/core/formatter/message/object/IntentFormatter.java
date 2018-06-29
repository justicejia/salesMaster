package com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object;

import android.content.Intent;

import com.sohu.focus.salesmaster.kernal.log.core.internal.util.ObjectToStringUtil;


/**
 * Format an Intent object to a string.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class IntentFormatter implements ObjectFormatter<Intent>{

    /**
     * Format an Intent object to a string.
     *
     * @param data the Intent object to format
     * @return the formatted string
     */
    @Override
    public String format(Intent data) {
        return ObjectToStringUtil.intentToString(data);
    }
}
