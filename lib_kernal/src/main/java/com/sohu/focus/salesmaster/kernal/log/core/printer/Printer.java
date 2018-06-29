package com.sohu.focus.salesmaster.kernal.log.core.printer;

import com.sohu.focus.salesmaster.kernal.log.core.printer.file.FilePrinter;

/**
 * A printer is used for printing the log to somewhere, like android shell, terminal
 * or file system.
 * <p>
 * There are 4 main implementation of Printer.
 * <br>{@link AndroidPrinter}, print log to android shell terminal.
 * <br>{@link ConsolePrinter}, print log to console via System.out.
 * <br>{@link FilePrinter}, print log to file system.
 * <br>{@link RemotePrinter}, print log to remote server, this is empty implementation yet.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface Printer {

    /**
     * Print log in new line.
     *
     * @param logLevel the level of log
     * @param tag      the tag of log
     * @param msg      the msg of log
     */
    void println(int logLevel, String tag, String msg);

    void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg);
}
