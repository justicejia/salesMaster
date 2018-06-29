package com.sohu.focus.salesmaster.kernal.log.core.formatter.message.object;

import android.os.Bundle;

import com.sohu.focus.salesmaster.kernal.log.core.internal.util.ObjectToStringUtil;


/**
 * Format an Bundle object to a string.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class BundleFormatter implements ObjectFormatter<Bundle> {

    /**
     * Format an Bundle object to a string.
     *
     * @param data the Bundle object to format
     * @return the formatted string
     */
    @Override
    public String format(Bundle data) {
        return ObjectToStringUtil.bundleToString(data);
    }
}
