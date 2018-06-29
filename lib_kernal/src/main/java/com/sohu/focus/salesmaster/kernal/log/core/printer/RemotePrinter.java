package com.sohu.focus.salesmaster.kernal.log.core.printer;

/**
 * Log {@link Printer} which should print the log to remote server.
 * <p>
 * This is just a empty implementation telling you that you can do
 * such thing, you can override {@link #println(int, String, String)} )} and sending the log by your
 * implementation.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class RemotePrinter implements Printer{

    @Override
    public void println(int logLevel, String tag, String msg) {
        // TODO: Send the log to your server.
    }

    @Override
    public void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        println(logLevel,tagStr,msg);
    }
}
