package com.sohu.focus.salesmaster.kernal.log.core.formatter.thread;

/**
 * Formatted stack trace looks like:
 * <br>Thread: thread-name
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class DefaultThreadFormatter implements ThreadFormatter {

    @Override
    public String format(Thread data) {
        return "Thread: " + data.getName();
    }
}
