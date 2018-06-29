package com.sohu.focus.salesmaster.kernal.log.core.printer.file.naming;

/**
 * Generates names for log files.
 *
 * Created by zhaoqiang on 2017/6/14.
 */

public interface FileNameGenerator {

    /**
     * Whether the generated file name will change or not.
     *
     * @return true if the file name is changeable
     */
    boolean isFileNameChangeable();

    /**
     * Generate file name for specified log level and timestamp.
     *
     * @param logLevel  the level of the log
     * @param timestamp the timestamp when the logging happen
     * @return the generated file name
     */
    String generateFileName(int logLevel, long timestamp);
}
