package com.sohu.focus.salesmaster.kernal.log.core.flattener;

/**
 * The log flattener used to flatten log elements(log level, tag and message) to a
 * single Charsequence.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface Flattener {

    /**
     * Flatten the log.
     *
     * @param logLevel the level of log
     * @param tag      the tag of log
     * @param message  the message of log
     * @return the formatted final log Charsequence
     */
    CharSequence flatten(int logLevel, String tag, String message);
}
