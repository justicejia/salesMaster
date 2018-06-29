package com.sohu.focus.salesmaster.kernal.log.core.printer;

/**
 * Created by zhaoqiang on 2017/6/25.
 */

public interface ILifecyclePrinter extends Printer {

    void initIfNeed();

    void close();

}
