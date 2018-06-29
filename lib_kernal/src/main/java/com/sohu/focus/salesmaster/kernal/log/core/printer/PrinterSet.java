package com.sohu.focus.salesmaster.kernal.log.core.printer;

/**
 * Created by zhaoqiang on 2017/6/25.
 */

public class PrinterSet implements Printer {

    private Printer[] delegates;

    public PrinterSet(Printer... printers) {
        delegates = printers;
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        for (Printer printer : delegates) {
            printer.println(logLevel, tag, msg);
        }
    }

    public void println2(int logLevel, String tagStr, String fileName, String funName, int line, String msg) {
        for (Printer printer : delegates) {
            printer.println2(logLevel, tagStr, fileName, funName, line, msg);
        }
    }
}
