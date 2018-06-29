package com.sohu.focus.salesmaster.kernal.log.core.interceptor;


import com.sohu.focus.salesmaster.kernal.log.core.LogCore;
import com.sohu.focus.salesmaster.kernal.log.core.LogItem;
import com.sohu.focus.salesmaster.kernal.log.core.LogConfiguration;

/**
 * Interceptors are used to intercept every log after formatting message, thread info and
 * stack trace info, and before printing, normally we can modify or drop the log.
 * <p>
 * Remember interceptors are ordered, which means earlier added interceptor will get the log
 * first.
 * <p>
 * If any interceptor remove the log(by returning null when {@link #intercept(com.sohu.focus.live.logger.core.LogItem)}),
 * then the interceptors behind that one won't receive the log, and the log won't be printed at all.
 *
 * @see LogConfiguration.Builder#addInterceptor(Interceptor)
 * @see LogCore#addInterceptor(Interceptor)
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface Interceptor {

    /**
     * Intercept the log.
     *
     * @param log the original log
     * @return the modified log, or null if the log should not be printed
     */
    LogItem intercept(LogItem log);
}
