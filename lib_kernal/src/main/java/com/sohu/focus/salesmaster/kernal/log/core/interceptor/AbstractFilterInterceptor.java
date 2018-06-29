package com.sohu.focus.salesmaster.kernal.log.core.interceptor;


import com.sohu.focus.salesmaster.kernal.log.core.LogItem;

/**
 * An filter interceptor is used to filter some specific logs out, this filtered logs won't be
 * printed by any printer.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public abstract class AbstractFilterInterceptor implements Interceptor {

    /**
     * {@inheritDoc}
     *
     * @param log the original log
     * @return the original log if it is acceptable, or null if it should be filtered out
     */
    @Override
    public LogItem intercept(LogItem log) {
        if (reject(log)) {
            // Filter this log out.
            return null;
        }
        return log;
    }

    /**
     * Whether specific log should be filtered out.
     *
     * @param log the specific log
     * @return true if the log should be filtered out, false otherwise
     */
    protected abstract boolean reject(LogItem log);
}
