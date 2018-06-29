package com.sohu.focus.salesmaster.kernal.log.core.flattener;


import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;

/**
 * Simply join the timestamp, log level, tag and message together.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class DefaultFlattener implements Flattener{

    @Override
    public CharSequence flatten(int logLevel, String tag, String message) {
        return Long.toString(System.currentTimeMillis())
                + '|' + LogLevel.getShortLevelName(logLevel)
                + '|' + tag
                + '|' + message;
    }
}
