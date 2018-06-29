package com.sohu.focus.salesmaster.kernal.log.core.formatter.message.throwable;


import com.sohu.focus.salesmaster.kernal.log.core.internal.util.StackTraceUtil;

/**
 * Simply put each stack trace(method name, source file and line number) of the throwable
 * in a single line.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class DefaultThrowableFormatter implements ThrowableFormatter {

    @Override
    public String format(Throwable tr) {
        return StackTraceUtil.getStackTraceString(tr);
    }
}