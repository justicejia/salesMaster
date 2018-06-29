package com.sohu.focus.salesmaster.kernal.log;


import com.sohu.focus.salesmaster.kernal.BuildConfig;
import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;

import java.util.Hashtable;

import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.ASSERT;
import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.DEBUG;
import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.ERROR;
import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.INFO;
import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.VERBOSE;
import static com.sohu.focus.salesmaster.kernal.log.core.LogLevel.WARN;

/**
 * Created by qiangzhao on 2016/11/10.
 */

public abstract class FocusLog  {

    public static final String TAG = "FocusLive";
    //是否使用xlog
    public static final boolean isUseXLog = true;
    public static final boolean isDebugging = BuildConfig.IS_DEBUG;
    //最低的file log级别
    public static final int FILE_LOG_LEVEL = FocusLog.isDebugging ? LogLevel.ALL : INFO;
    protected static Hashtable<String, FocusLog> sLoggerTable = new Hashtable<>();
    protected String mDeveloperName;

    public FocusLog(String name) {
        mDeveloperName = name;
    }

    /**
     * 具体实现log方法，在FocusLogWrapper中实现
     */
    abstract public void println(int priority, String tag, Object msg, Throwable tr);


    public void v(String tag, Object msg, Throwable tr) {
        println(VERBOSE, tag, msg, tr);
    }

    public void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public void v(Object msg) {
        v(TAG, msg, null);
    }

    public void d(String tag, Object msg, Throwable tr) {
        println(DEBUG, tag, msg, tr);
    }

    public void d(String tag, Object msg) {
        d(tag, msg, null);
    }

    public void d(Object msg) {
        d(TAG, msg, null);
    }

    public void i(String tag, Object msg, Throwable tr) {
        println(INFO, tag, msg, tr);
    }

    public void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public void i(Object msg) {
        i(TAG, msg, null);
    }

    public void w(String tag, Object msg, Throwable tr) {
        println(WARN, tag, msg, tr);
    }

    public void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public void w(Object msg) {
        w(TAG, msg, null);
    }

    public void w(String tag, Throwable tr) {
        w(tag, null, tr);
    }

    public void e(String tag, Object msg, Throwable tr) {
        println(ERROR, tag, msg, tr);
    }

    public void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public void e(Object msg) {
        e(TAG, msg, null);
    }

    public void wtf(String tag, Object msg, Throwable tr) {
        println(ASSERT, tag, msg, tr);
    }

    public void wtf(String tag, Object msg) {
        wtf(tag, msg, null);
    }

    public void wtf(String tag, Throwable tr) {
        wtf(tag, null, tr);
    }

    public void println(int priority, String tag, String msg) {
        println(priority, tag, msg, null);
    }

}
