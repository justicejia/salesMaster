package com.sohu.focus.salesmaster.kernal.log.core.printer;

import android.os.Looper;
import android.os.Process;

import com.sohu.focus.salesmaster.kernal.BaseApplication;
import com.sohu.focus.salesmaster.kernal.log.FocusLog;
import com.sohu.focus.salesmaster.kernal.log.core.LogLevel;
import com.sohu.focus.salesmaster.kernal.log.core.printer.file.FilePrinter;
import com.sohu.focus.salesmaster.kernal.utils.PreferenceManager;
import com.sohu.focus.salesmaster.kernal.utils.StorageUtil;
import com.tencent.mars.xlog.Log;
import com.tencent.mars.xlog.Xlog;

import java.io.File;

import static com.sohu.focus.salesmaster.kernal.utils.StorageUtil.DEFAULT_LOG_DIR;


/**
 * Created by zhaoqiang on 2017/6/23.
 */

public class XLogPrinter extends Log implements ILifecyclePrinter {

    public static final String IS_OPEN = "is_open";

    public static final int STATUS_CLOSE = 0;
    public static final int STATUS_OPEN = 1;
    public static final int STATUS_UNKNOWN = -1;

    static {
        System.loadLibrary("stlport_shared");
        System.loadLibrary("marsxlog");
    }

    private String folderPath;
    private int saveLogLevel = FocusLog.FILE_LOG_LEVEL;

    XLogPrinter(XLogPrinter.Builder builder) {
        folderPath = builder.folderPath;
        saveLogLevel = builder.saveLogLevel;

        checkLogFolder();
        initIfNeed();
        setLogImp(new Xlog());
    }

    private void initXlog() {
        final String logPath = new File(StorageUtil.getCacheDir(), DEFAULT_LOG_DIR).getPath();

        // this is necessary, or may cash for SIGBUS
        final String cachePath = BaseApplication.getApplication().getFilesDir() + "/focus_cache";

        //init xlog
        Xlog.appenderOpen(FocusLog.isDebugging ? Xlog.LEVEL_ALL : Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, FocusLog.TAG);
        Xlog.setConsoleLogOpen(FocusLog.isDebugging);

        PreferenceManager.getInstance().saveData(IS_OPEN, STATUS_OPEN);
    }

    /**
     * Make sure the folder of log file exists.
     */
    private void checkLogFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        if (logLevel < saveLogLevel)
            return;
        logInternal(logLevel, tag, msg);
    }

    @Override
    public void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        if (logLevel < saveLogLevel)
            return;
        Xlog.logWrite2(logLevel, tagStr, fileName, funName, line, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), msg);
    }

    public void initIfNeed() {
        int status = PreferenceManager.getInstance().getIntData(IS_OPEN, STATUS_UNKNOWN);
        if(status != STATUS_OPEN)
            initXlog();
    }

    public void close() {
        appenderClose();
        PreferenceManager.getInstance().saveData(IS_OPEN, STATUS_CLOSE);
    }

    private void logInternal(int logLevel, String tag, String msg) {
        switch (logLevel) {
            case LogLevel.VERBOSE:
                v(tag, msg);
                break;
            case LogLevel.DEBUG:
                d(tag, msg);
                break;
            case LogLevel.INFO:
                i(tag, msg);
                break;
            case LogLevel.WARN:
                w(tag, msg);
                break;
            case LogLevel.ERROR:
                e(tag, msg);
                break;
        }
    }

    public static class Builder {

        /**
         * The folder path of log file.
         */
        String folderPath;

        /**
         * Lowest File Log Level
         */
        private int saveLogLevel = LogLevel.DEBUG;

        /**
         * Construct a builder.
         *
         * @param folderPath the folder path of log file
         */
        public Builder(String folderPath) {
            this.folderPath = folderPath;
        }

        public XLogPrinter.Builder lowestLogLevel(int level) {
            this.saveLogLevel = level;
            return this;
        }

        /**
         * Build configured {@link FilePrinter} object.
         *
         * @return the built configured {@link FilePrinter} object
         */
        public XLogPrinter build() {
            return new XLogPrinter(this);
        }
    }
}
