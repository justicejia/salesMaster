package com.sohu.focus.salesmaster.kernal.log.core.printer;


import com.sohu.focus.salesmaster.kernal.log.core.flattener.Flattener;
import com.sohu.focus.salesmaster.kernal.log.core.internal.DefaultsFactory;

/**
 * Log {@link Printer} using {@code System.out.println(String)}.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public class ConsolePrinter implements Printer {

    /**
     * The log flattener when print a log.
     */
    private Flattener flattener;

    /**
     * Constructor.
     */
    public ConsolePrinter() {
        this.flattener = DefaultsFactory.createFlattener();
    }

    /**
     * Constructor.
     *
     * @param flattener the log flattener when print a log
     */
    public ConsolePrinter(Flattener flattener) {
        this.flattener = flattener;
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        String flattenedLog = flattener.flatten(logLevel, tag, msg).toString();
        System.out.println(flattenedLog);
    }

    @Override
    public void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        String flattenedLog = flattener.flatten(logLevel, tagStr, msg).toString();
        System.out.println(flattenedLog);
    }
}

