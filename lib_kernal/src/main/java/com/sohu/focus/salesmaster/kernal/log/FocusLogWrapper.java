package com.sohu.focus.salesmaster.kernal.log;

import android.util.Log;

import com.sohu.focus.salesmaster.kernal.log.core.LogCore;
import com.sohu.focus.salesmaster.kernal.log.core.LogConfiguration;
import com.sohu.focus.salesmaster.kernal.utils.CommonUtils;


/**
 * Created by qiangzhao on 2016/11/10.
 */

public class FocusLogWrapper extends FocusLog {

    private StringBuilder sb = new StringBuilder();

    public FocusLogWrapper(String name) {
        super(name);
    }

    /**
     * @param className
     * @return
     */
    protected static FocusLogWrapper getLogger(String className) {
        FocusLogWrapper classLogger = (FocusLogWrapper) sLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new FocusLogWrapper(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }

    @Override
    public void println(int priority, String tag, Object msg, Throwable tr) {
        String useMsg;
        if (msg == null || CommonUtils.isEmpty(msg.toString())) {
            useMsg = "";
        } else {
            useMsg = msg.toString();
        }

        if (CommonUtils.isEmpty(tag)) {
            tag = TAG;
        }

        if (tr != null) {
            useMsg += "\n" + Log.getStackTraceString(tr);
        }

        printLog(priority, tag, useMsg);

    }

    private void printLog(int level, String tag, String msg) {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().contains("FocusLog")) {
                continue;
            }
            sb.setLength(0);
            if (LogCore.getLogMode() == LogConfiguration.LOG_MODE_PRINTLN) {
                sb.append(mDeveloperName);
                sb.append("[ ");
                sb.append(Thread.currentThread().getName());
                sb.append(": ");
                sb.append(st.getFileName());
                sb.append(":");
                sb.append(st.getLineNumber());
                sb.append(" ");
                sb.append(st.getMethodName());
                sb.append(" ]");
                sb.append("-");
                sb.append("msg");
                LogCore.log(level, tag, sb.toString());
            } else if (LogCore.getLogMode() == LogConfiguration.LOG_MODE_PRINTLN2) {
                sb.append(mDeveloperName);
                sb.append("[ ");
                sb.append(Thread.currentThread().getName());
                sb.append(" ] : ");
                sb.append(msg);
                LogCore.log(level, tag, st.getFileName(), st.getMethodName(), st.getLineNumber(),
                        sb.toString());
            }

            return;
        }
        return;
    }

}
